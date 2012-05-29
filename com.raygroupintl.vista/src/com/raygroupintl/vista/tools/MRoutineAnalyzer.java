package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.parsetree.visitor.FanoutRecorder;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TRoutine;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.PackageInfo;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public void writeErrors(final TFRoutine tf, final ErrorExemptions exemptions, String outputPath) throws IOException, SyntaxErrorException {
		int errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		final String eol = TRoutine.getEOL();
		List<Path> paths = FileSupply.getAllMFiles();
		ErrorRecorder ev = new ErrorRecorder();
		for (Path path : paths) {
			TRoutine r = tf.tokenize(path);			
			final String name = r.getName();
			if (! exemptions.containsRoutine(name)) {
				Set<LineLocation> locations = exemptions.getLines(name);
				List<ObjectInRoutine<MError>> errors = ev.visitErrors(r.getNode(), locations);
				if (errors.size() > 0) {
					os.write((eol + eol + r.getName() + eol).getBytes());
					errorCount += errors.size();
					LineLocation lastLocation = new LineLocation("", 0);
					for (ObjectInRoutine<MError> error: errors) {
						if (! error.getLocation().equals(lastLocation)) {
							lastLocation = error.getLocation();
							String offset = (lastLocation.getOffset() == 0 ? "" : '+' + String.valueOf(lastLocation.getOffset()));
							os.write(("  " + lastLocation.getTag() + offset + eol).getBytes());
						}
						os.write(("    " + error.getObject().getText() + eol).getBytes());
					}
					errorCount += errors.size();
				}
			}			
		}
		os.write((eol + eol + "Number Errors: " + String.valueOf(errorCount) + eol).getBytes());
		os.close();
	}
	
	public void writeErrors(CLIParams options, TFRoutine topToken) throws IOException, SyntaxErrorException {		
		String outputFile = options.outputFile; 
		ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		this.writeErrors(topToken, exemptions, outputFile);				
	}

	
	public void writeFanout(CLIParams options, TFRoutine topToken) throws IOException, SyntaxErrorException {
		RepositoryInfo ri = RepositoryInfo.getInstance();
		
		FileSupply fs = new FileSupply();
		for (String packageName : options.packages) {
			PackageInfo pi = ri.getPackage(packageName);
			String dir = pi.getDirectoryName();
			fs.addPackage(dir);
		}		
		List<Path> paths = fs.getFiles();
		FanoutRecorder fr = new FanoutRecorder();
		Map<String, Map<LineLocation, List<Fanout>>> allFanouts = new HashMap<String, Map<LineLocation, List<Fanout>>>();
		for (Path path : paths) {			
			TRoutine r = topToken.tokenize(path);
			Routine node = r.getNode();
			Map<LineLocation, List<Fanout>> routineFanouts = fr.getFanouts(node);
			allFanouts.put(node.getKey(), routineFanouts);
		}
				
		String outputFile = options.outputFile;
		File file = new File(outputFile);
		FileOutputStream os = new FileOutputStream(file);
		String eol = TRoutine.getEOL();
		for (String routineName : allFanouts.keySet()) {
			os.write(("Routine Name: " + routineName + eol).getBytes());
			Map<LineLocation, List<Fanout>> fanouts = allFanouts.get(routineName);
			for (LineLocation location : fanouts.keySet()) {
				os.write(("  Location: " + location.toString() + eol).getBytes());
				List<Fanout> ffouts = fanouts.get(location);
				for (Fanout fout : ffouts) {
					String lbl = fout.getTag();
					String rou = fout.getRoutineName();
					if (rou != null) {
						rou = "^" + rou;
					} else {
						rou = "";
					}
					if (lbl == null) {
						lbl = "";
					}					
					os.write(("    "  + lbl + rou + eol).getBytes());					
				}
			}
			os.write(eol.getBytes());
		}
		os.write(eol.getBytes());
	}
	
	public static void main(String[] args) {
		try {
			CLIParams options = CLIParams.getInstance(args);
			if (options == null) return;

			MRoutineAnalyzer m = new MRoutineAnalyzer();
			MTFSupply supply = MTFSupply.getInstance(MVersion.CACHE);
			TFRoutine tf = TFRoutine.getInstance(supply);
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				m.writeErrors(options, tf);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				m.writeFanout(options, tf);
				return;
			}
			LOGGER.log(Level.SEVERE, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
