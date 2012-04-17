package com.raygroupintl.vista.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFCommand;
import com.raygroupintl.vista.mtoken.TFOperator;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.struct.MLineLocation;

public class MRoutineAnalyzer {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	private static int errorCount = 0;
	
	public void writeErrors(final MVersion version, final ErrorExemptions exemptions, String outputPath) throws IOException {
		errorCount = 0;
		final File file = new File(outputPath);
		final FileOutputStream os = new FileOutputStream(file);
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					TRoutine r = TRoutine.getInstance(version, path);
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
		//v.addPath("C:\\Users\\Afsin\\git\\VistA-FOIA\\Packages\\Visual Impairment Service Team");
		v.addVistAFOIA();
		v.run();
		String eol = TRoutine.getEOL();
		os.write((eol + eol + "Number Errors: " + String.valueOf(errorCount) + eol).getBytes());
		os.close();
	}
		
	public static void main(String[] args) throws Exception {
		MVersion version = MVersion.CACHE;
		
		TFOperator.addOperator(">=");
		TFOperator.addOperator("<=");
		TFOperator.addOperator("&&");
		TFOperator.addOperator("||");
				
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
		TFCommand.addCommand("ZRELPAGE");
		TFCommand.addCommand("ZSYSTEM");
		TFCommand.addCommand("ZLINK");		
		TFCommand.addCommand("ZESCAPE");
		TFCommand.addCommand("ZITRAP");
		TFCommand.addCommand("ZGETPAGE");
		
		if (args.length == 0) {
			LOGGER.info("A path needs to be specified.");
			return;
		}
		String outputFile = args[0]; 
		MRoutineAnalyzer m = new MRoutineAnalyzer();
		
		ErrorExemptions exemptions = new ErrorExemptions();
		exemptions.addLine("ANRVRRL", "BEGIN", 3);
		exemptions.addLine("ANRVRRL", "A1R", 2);
		exemptions.addLine("DINVMSM", "T0", 2);
		exemptions.addLine("DINVMSM", "T1", 2);
		exemptions.addLine("ZOSVMSM", "T0", 2);
		exemptions.addLine("ZOSVMSM", "T1", 2);
		exemptions.addLine("MUSMCR3", "BEG", 2);
		exemptions.addLine("MUSMCR3", "BEG", 6);
		exemptions.addLine("MUSMCR1", "EN11", 3);
		exemptions.addLine("DENTA14", "P1", 0);
		exemptions.addLine("HLOTCP", "RETRY", 8);
		exemptions.addLine("HLOTCP", "RETRY", 9);
		exemptions.addLine("HLOTCP", "RETRY", 14);
		exemptions.addLine("RGUTRRC", "JOBIT", 2);
		exemptions.addLine("ZISHMSU", "OPEN", 9);
		exemptions.addLine("ZISG3", "SUBITEM", 4);
		exemptions.addLine("PRCBR1", "LOCK", 1);
		exemptions.addLine("ZISTCP", "CVXD", 2);
		exemptions.addLine("ZOSVKRV", "JT", 11);
		exemptions.addLine("ZTMB", "RESTART", 21);
		exemptions.addLine("LEXAR7", "MAILQ", 1);
		exemptions.addLine("XMRTCP", "SOC25", 3);
		exemptions.addLine("ORRDI2", "TCOLD", 6);
		exemptions.addLine("PSSFDBRT", "POST", 21);
		exemptions.addLine("PSSHTTP", "PEPSPOST", 26);
		exemptions.addLine("PSSHTTP", "PEPSPOST", 34);
		exemptions.addRoutine("PSORELD1");
		
		m.writeErrors(version, exemptions, outputFile);		
	}
}
