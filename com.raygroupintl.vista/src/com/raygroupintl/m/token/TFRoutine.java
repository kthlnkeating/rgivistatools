package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.vista.struct.MRoutineContent;

public class TFRoutine {
	private TFLine tfLine;
	
	private TFRoutine(MVersion version) {
		this.tfLine = TFLine.getInstance(version);
	}
	
	public TRoutine tokenize(MRoutineContent content) throws SyntaxErrorException {
		String name = content.getName();
		TRoutine result = new TRoutine(name);
		for (String line : content.getLines()) {
			TLine tokens = (TLine) this.tfLine.tokenize(line, 0);
			result.add(tokens);			
		}		
		return result;
	}
	
	public TRoutine tokenize(Path path) throws IOException,  SyntaxErrorException {
		MRoutineContent content = MRoutineContent.getInstance(path);					
		TRoutine r = this.tokenize(content);
		return r;
	}
		
	public TRoutine tokenize(String name, String line, int fromIndex) throws SyntaxErrorException {
		int endIndex = line.length();
		int index = fromIndex;
		TRoutine result = new TRoutine(name);
		while (index < endIndex) {
			TLine tokens = (TLine) this.tfLine.tokenize(line, index);
			index += tokens == null ? 0 : tokens.getStringSize();
			result.add(tokens);						
		}
		
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
