package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Text;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.vista.struct.MRoutineContent;

public class TFRoutine {
	private TokenFactory tfLine;
	
	private TFRoutine(MVersion version) {
		this.tfLine = MTFSupply.getInstance(version).line;
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
	public static TFRoutine getInstance(MVersion version) {
		return new TFRoutine(version);
	}	
}
