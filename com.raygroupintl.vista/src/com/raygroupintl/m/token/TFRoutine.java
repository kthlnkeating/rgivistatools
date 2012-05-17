package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TFRoutine {
	private TokenFactory tfLine;
	
	private TFRoutine(MTFSupply supply) {
		this.tfLine = supply.line;
	}
	
	public static TLine recoverFromError(String line, SyntaxErrorException e) {
		Token error = new TSyntaxError(0, line, 0);
		Token[] lineResult = {error, null, null, null, null};
		return new TLine(Arrays.asList(lineResult));
	}
	
	public TRoutine tokenize(MRoutineContent content) {
		String name = content.getName();
		TRoutine result = new TRoutine(name);
		int index = 0;
		String tagName = "";
		for (String line : content.getLines()) {
			TLine tokens = null;
			try {
				Text text = new Text(line);
				tokens = (TLine) this.tfLine.tokenize(text);
			} catch (SyntaxErrorException e) {
				tokens = recoverFromError(line, e);
			}
			String lineTagName = tokens.getTag();
			if (lineTagName != null) {
				tagName = lineTagName;
				index = 0;
			}
			tokens.setIdentifier(tagName, index);
			result.add(tokens);
			++index;
		}		
		return result;
	}
	
	public TRoutine tokenize(Path path) throws IOException,  SyntaxErrorException {
		MRoutineContent content = MRoutineContent.getInstance(path);					
		TRoutine r = this.tokenize(content);
		return r;
	}
		
/*	public TRoutine tokenize(String name, String line, int fromIndex) throws SyntaxErrorException {
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
*/
	public static TFRoutine getInstance(MTFSupply supply) {
		return new TFRoutine(supply);
	}	
}
