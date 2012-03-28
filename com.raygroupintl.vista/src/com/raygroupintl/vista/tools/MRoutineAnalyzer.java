package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.mtoken.TFOperator;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	public void writeErrors(String outputPath) throws IOException {
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					TRoutine r = TRoutine.getInstance(path);
					r.writeErrors(os);
				} catch(Throwable t) {
					String fileName = path.getFileName().toString();
					MRoutineAnalyzer.LOGGER.info("Exception: " + fileName);
				}
			}
		};
		MFileVisitor v = new MFileVisitor(action);
		v.addVistAFOIA();
		v.run();
		os.close();
	}
		
	public static void main(String[] args) throws Exception {
		TFOperator.addOperator(">=");
		TFOperator.addOperator("<=");
		TFOperator.addOperator("&&");
		
		TFIntrinsic.addVariable("ZA");
		TFIntrinsic.addVariable("ZB");
		TFIntrinsic.addVariable("ZE");
		TFIntrinsic.addVariable("ZT");
		TFIntrinsic.addVariable("ZV");

		TFIntrinsic.addFunction("ZC");
		TFIntrinsic.addFunction("ZF");
		TFIntrinsic.addFunction("ZU");
		TFIntrinsic.addFunction("ZUTIL");
		TFIntrinsic.addFunction("ZTRNLNM");		
		
		if (args.length == 0) {
			LOGGER.info("A path needs to be specified.");
			return;
		}
		String outputFile = args[0]; 
		MRoutineAnalyzer m = new MRoutineAnalyzer();
		m.writeErrors(outputFile);		
	}
}
