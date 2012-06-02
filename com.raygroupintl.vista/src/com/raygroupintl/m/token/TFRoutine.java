package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFRoutine {
	private TokenFactory tfLine;
	private AdapterSupply mAdapterSupply = new MAdapterSupply();
	
	public TFRoutine(MTFSupply supply) {
		this.tfLine = supply.line;
	}
	
	public static TLine recoverFromError(String line, SyntaxErrorException e) {
		Token error = new TSyntaxError(0, new StringPiece(line), 0);
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
				tokens = (TLine) this.tfLine.tokenize(text, this.mAdapterSupply);
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
}
