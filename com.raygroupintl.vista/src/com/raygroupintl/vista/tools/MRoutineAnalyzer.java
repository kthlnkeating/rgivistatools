package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.struct.MError;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public void writeErrors(String outputPath) throws IOException {
		final File file = new File(outputPath);
		final FileWriter w = new FileWriter(file);
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					TRoutine r = TRoutine.getInstance(path);
					List<MError> errors = r.getErrors();
					if (errors != null) for (MError error : errors){
						w.write(path.getFileName().toString());
						w.write(": ");
						w.write(error.getText());
						w.write("\n");
					}
				} catch(Throwable t) {
					MRoutineAnalyzer.LOGGER.info("Exception");
				}
			}
		};
		MFileVisitor v = new MFileVisitor(action);
		v.addVistAFOIA();
		v.run();
		w.close();
	}
		
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			LOGGER.info("A path needs to be specified.");
			return;
		}
		String outputFile = args[0]; 
		MRoutineAnalyzer m = new MRoutineAnalyzer();
		m.writeErrors(outputFile);		
	}
}
