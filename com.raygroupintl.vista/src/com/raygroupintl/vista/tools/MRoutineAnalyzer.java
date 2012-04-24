package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.struct.Fanout;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.RoutineFanouts;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFRoutine;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.PackageInfo;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.struct.MLocationedError;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public void writeErrors(final TFRoutine tf, final ErrorExemptions exemptions, String outputPath) throws IOException {
		int errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		final String eol = TRoutine.getEOL();
		List<Path> paths = FileSupply.getAllMFiles();
		for (Path path : paths) {
			TRoutine r = tf.tokenize(path);
			final String name = r.getName();
			if (! exemptions.containsRoutine(name)) {
				Set<LineLocation> locations = exemptions.getLines(name);
				List<MLocationedError> errors = r.getErrors(locations);
				if (errors.size() > 0) {
					os.write((eol + eol + r.getName() + eol).getBytes());
					errorCount += errors.size();
					LineLocation lastLocation = new LineLocation("", 0);
					for (MLocationedError error: errors) {
						if (! error.getLocation().equals(lastLocation)) {
							lastLocation = error.getLocation();
							String offset = (lastLocation.getOffset() == 0 ? "" : '+' + String.valueOf(lastLocation.getOffset()));
							os.write(("  " + lastLocation.getTag() + offset + eol).getBytes());
						}
						os.write(("    " + error.getError().getText() + eol).getBytes());
					}
					errorCount += errors.size();
				}
			}			
		}
		os.write((eol + eol + "Number Errors: " + String.valueOf(errorCount) + eol).getBytes());
		os.close();
	}
	
	public void writeErrors(CLIParams options) throws IOException {		
		String outputFile = options.outputFile; 
		TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
		ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		this.writeErrors(tf, exemptions, outputFile);				
	}

	
	public void writeFanout(CLIParams options) throws IOException {
		RepositoryInfo ri = RepositoryInfo.getInstance();
		
		FileSupply fs = new FileSupply();
		for (String packageName : options.packages) {
			PackageInfo pi = ri.getPackage(packageName);
			String dir = pi.getDirectoryName();
			fs.addPackage(dir);
		}		
		List<Path> paths = fs.getFiles();

		TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
		List<RoutineFanouts> fanouts = new ArrayList<RoutineFanouts>();
		for (Path path : paths) {			
			TRoutine r = tf.tokenize(path);
			RoutineFanouts rfo = r.getFanouts();
			fanouts.add(rfo);
		}
				
		String outputFile = options.outputFile;
		File file = new File(outputFile);
		FileOutputStream os = new FileOutputStream(file);
		String eol = TRoutine.getEOL();
		for (RoutineFanouts rfo : fanouts) {
			os.write(("Routine Name: " + rfo.getRoutineName() + eol).getBytes());
			for (LineLocation location : rfo.getFanoutLocations()) {
				os.write(("  Location: " + location.toString() + eol).getBytes());
				for (Fanout fout : rfo.getFanouts(location)) {
					os.write(("    "  + fout.getLocation().toString() + "^" + fout.getRoutineName() + eol).getBytes());					
				}
			}
			os.write(eol.getBytes());
		}
		os.write(eol.getBytes());
	}
	
	public static void main(String[] args) throws Exception {
		try {
			CLIParams options = CLIParams.getInstance(args);
			if (options == null) return;

			MRoutineAnalyzer m = new MRoutineAnalyzer();
			String at = options.analysisType;
			if (at.equalsIgnoreCase("error")) {
				m.writeErrors(options);
				return;
			}
			if (at.equalsIgnoreCase("fanout")) {
				m.writeFanout(options);
				return;
			}
			LOGGER.log(Level.SEVERE, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
