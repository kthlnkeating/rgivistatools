package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.TFCommand;
import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.mtoken.TFOperator;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	private static int errorCount = 0;
	
	public void writeErrors(String outputPath) throws IOException {
		errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					TRoutine r = TRoutine.getInstance(path);
					errorCount += r.writeErrors(os);
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
		TFOperator.addOperator(">=");
		TFOperator.addOperator("<=");
		TFOperator.addOperator("&&");
		
		TFIntrinsic.addVariable("ZA");
		TFIntrinsic.addVariable("ZB");
		TFIntrinsic.addVariable("ZC");
		TFIntrinsic.addVariable("ZE");
		TFIntrinsic.addVariable("ZH");
		TFIntrinsic.addVariable("ZJ");
		TFIntrinsic.addVariable("ZR");
		TFIntrinsic.addVariable("ZT");
		TFIntrinsic.addVariable("ZV");
		TFIntrinsic.addVariable("ZIO");	
		TFIntrinsic.addVariable("ZIOS");	
		TFIntrinsic.addVariable("ZVER");
		TFIntrinsic.addVariable("ZEOF");
		TFIntrinsic.addVariable("ZNSPACE");
		TFIntrinsic.addVariable("ZRO");
		TFIntrinsic.addVariable("R");
		TFIntrinsic.addVariable("ZS");
		TFIntrinsic.addVariable("ZROUTINES");
		TFIntrinsic.addVariable("ETRAP");
		TFIntrinsic.addVariable("ZTIMESTAMP");
		TFIntrinsic.addVariable("ZERROR");
		TFIntrinsic.addVariable("ZCMDLINE");
		TFIntrinsic.addVariable("ZPOSITION");
		TFIntrinsic.addFunction("ZC");
		TFIntrinsic.addFunction("ZF");
		TFIntrinsic.addFunction("ZJ");
		TFIntrinsic.addFunction("ZU");
		TFIntrinsic.addFunction("ZUTIL");
		TFIntrinsic.addFunction("ZTRNLNM");	
		TFIntrinsic.addFunction("ZBOOLEAN");	
		TFIntrinsic.addFunction("ZDEV");	
		TFIntrinsic.addFunction("ZGETDV");
		TFIntrinsic.addFunction("ZSORT");
		TFIntrinsic.addFunction("ZESCAPE");
		TFIntrinsic.addFunction("ZSEARCH");
		TFIntrinsic.addFunction("ZPARSE");
		TFIntrinsic.addFunction("ZCONVERT");
		TFIntrinsic.addFunction("ZDVI");
		TFIntrinsic.addFunction("ZGETDVI");
		TFIntrinsic.addFunction("ZOS");
		TFIntrinsic.addFunction("ZINTERRUPT");
		TFIntrinsic.addFunction("ZJOB");
		TFIntrinsic.addFunction("ZBITSTR");
		TFIntrinsic.addFunction("ZBITXOR");
		TFIntrinsic.addFunction("LISTGET");
		TFIntrinsic.addFunction("ZDEVSPEED");
		TFIntrinsic.addFunction("ZGETJPI");
		TFIntrinsic.addFunction("ZGETSYI");
		TFIntrinsic.addFunction("ZUTIL");
		
		TFCommand.addCommand("ZB");
		TFCommand.addCommand("ZS");
		TFCommand.addCommand("ZC");
		TFCommand.addCommand("ZR");
		TFCommand.addCommand("ZI");
		TFCommand.addCommand("ZQ");
		TFCommand.addCommand("ZT");
		TFCommand.addCommand("ZU");
		TFCommand.addCommand("ZSHOW");
		TFCommand.addCommand("ZNSPACE");
		TFCommand.addCommand("ZETRAP");
		TFCommand.addCommand("ESTART");
		TFCommand.addCommand("ESTOP");
		TFCommand.addCommand("ABORT");
		TFCommand.addCommand("ZSYSTEM");
		TFCommand.addCommand("ZLINK");		
		TFCommand.addCommand("ZESCAPE");
		TFCommand.addCommand("ZITRAP");
		
		if (args.length == 0) {
			LOGGER.info("A path needs to be specified.");
			return;
		}
		String outputFile = args[0]; 
		MRoutineAnalyzer m = new MRoutineAnalyzer();
		m.writeErrors(outputFile);		
	}
}
