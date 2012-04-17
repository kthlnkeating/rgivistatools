package com.raygroupintl.vista.mtoken;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Scanner;

public class TFRoutine {
	private TFLine tfLine;
	
	private TFRoutine(MVersion version) {
		this.tfLine = TFLine.getInstance(version);
	}
	
	private TRoutine tokenize(String fileName, Scanner scanner) {
		TRoutine result = new TRoutine(fileName);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			TLine tokens = (TLine) this.tfLine.tokenize(line, 0);
			result.add(tokens);
		}
		return result;				
	}

	public TRoutine tokenize(String fileName, InputStream is) {
		Scanner scanner = new Scanner(is);
		TRoutine result = this.tokenize(fileName, scanner);
		scanner.close();
		return result;				
	}

	public TRoutine tokenize(Path path) throws IOException {
		String fileName = path.getFileName().toString();
		Scanner scanner = new Scanner(path);
		TRoutine result = this.tokenize(fileName, scanner);
		scanner.close();
		return result;		
	}
	
	public static TFRoutine getInstance(MVersion version) {
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

		return new TFRoutine(version);
	}	
}
