package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFRoutine;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.struct.MLineLocation;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	private static int errorCount = 0;
	
	public void writeErrors(final TFRoutine tf, final ErrorExemptions exemptions, String outputPath) throws IOException {
		errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					TRoutine r = tf.tokenize(path);
					final String name = r.getName();
					if (! exemptions.containsRoutine(name)) {
						Set<MLineLocation> locations = exemptions.getLines(name);
						errorCount += r.writeErrors(os, locations);
					}
				} catch(Throwable t) {
					String fileName = path.getFileName().toString();
					MRoutineAnalyzer.LOGGER.info("Exception: " + fileName);
				}
			}
		};
		
		MFileVisitor v = new MFileVisitor(action);
		v.addVistAFOIA();
		v.run();
		String eol = TRoutine.getEOL();
		os.write((eol + eol + "Number Errors: " + String.valueOf(errorCount) + eol).getBytes());
		os.close();
	}
		
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			LOGGER.info("A path needs to be specified.");
			return;
		}
		String outputFile = args[0]; 
		MRoutineAnalyzer m = new MRoutineAnalyzer();
		TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
		ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		m.writeErrors(tf, exemptions, outputFile);		
	}
}
