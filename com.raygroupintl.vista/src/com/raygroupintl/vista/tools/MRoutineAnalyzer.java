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

import com.raygroupintl.util.CLIParamMgr;
import com.raygroupintl.util.CLIParameter;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFRoutine;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.struct.MLineLocation;
import com.raygroupintl.vista.struct.MLocationedError;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public static class CLIParams {
		@CLIParameter
		public List<String> reportContent = new ArrayList<String>();
		
		@CLIParameter(names={"-p", "--package"})
		public List<String> packages = new ArrayList<String>();
		
		@CLIParameter(names={"-o", "--output"})
		public String outputFile;
		
		@CLIParameter(names={"-t", "--type"})
		public String analysisType = "error";
		
		public boolean validate() {
			if (this.outputFile == null) {
				LOGGER.log(Level.SEVERE, "No output file is specified.");
				return false;
			}
			return true;
		}
	}

/*	private static class WriteFanOut implements IFileAction {
		private CLIParams options;
		private FanoutInfo fanoutInfo = new FanoutInfo();
		
		public WriteFanOut(CLIParams options) {
			this.options = options;
		}
		
		@Override
		public void handle(Path path) {
			TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
			TRoutine r = tf.tokenize(path);
			FanoutInfo fout = r.getFanoutInfo();
			this.fanoutInfo.add(fout);
			
			try {
				TRoutine r = tf.tokenize(path);
				r.get
				
				
				final String name = r.getName();
				if (! exemptions.containsRoutine(name)) {
					Set<MLineLocation> locations = exemptions.getLines(name);
					List<MLocationedError> errors = r.getErrors(locations);
					if (errors.size() > 0) {
						os.write((eol + eol + r.getName() + eol).getBytes());
						errorCount += errors.size();
						MLineLocation lastLocation = new MLineLocation("", 0);
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
			} catch(Throwable t) {
				String fileName = path.getFileName().toString();
				MRoutineAnalyzer.LOGGER.info("Exception: " + fileName);
			}
		}		
	}*/
	
	
	private static int errorCount = 0;
	
	public void writeErrors(final TFRoutine tf, final ErrorExemptions exemptions, String outputPath) throws IOException {
		errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		final String eol = TRoutine.getEOL();
		List<Path> paths = FileSupply.getAllMFiles();
		for (Path path : paths) {
			TRoutine r = tf.tokenize(path);
			final String name = r.getName();
			if (! exemptions.containsRoutine(name)) {
				Set<MLineLocation> locations = exemptions.getLines(name);
				List<MLocationedError> errors = r.getErrors(locations);
				if (errors.size() > 0) {
					os.write((eol + eol + r.getName() + eol).getBytes());
					errorCount += errors.size();
					MLineLocation lastLocation = new MLineLocation("", 0);
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
		//String outputFile = options.outputFile;
		//RepositoryInfo ri = RepositoryInfo.getInstance();
		//MFileVisitor v = new MFileVisitor(action);
	}
	
	private static CLIParams getOptions(String[] args) {
		try {
			CLIParams options = CLIParamMgr.parse(CLIParams.class, args);
			return options;
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Invalid options", t);
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		try {
			CLIParams options = getOptions(args);
			if ((options == null) || (! options.validate())) {
				return;
			}
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
			LOGGER.log(Level.WARNING, "Unknown analysis type " + at);
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE, "Unexpected error", t);
			return;
		}
	}
}
