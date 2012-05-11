package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TokenStore;
import com.raygroupintl.vista.struct.MRoutineContent;

public class TFRoutine {
	private TokenFactory tfLine;
	
	private TFRoutine(MVersion version) {
		this.tfLine = MTFSupply.getInstance(version).line;
	}
	
	public static TLine recoverFromError(String line, SyntaxErrorException e) {
		List<TokenStore> stores = e.getTokenStores();
		TSyntaxError t = new TSyntaxError(e.getCode(), line, e.getLocation());
		if (stores != null) {
			int index = stores.size()-1;
			TokenStore lineTokens = stores.get(index);
			if (lineTokens.size() >= 4) {
				--index;
				Token[] arrays = lineTokens.toArray();
				int currentLocation = lineTokens.toToken().getStringSize();
				if (index < 0) {
					TList ee = new TList(t);
					arrays[4] = ee;
					return new TLine(arrays);
				} else {
					TokenStore commands = stores.get(index);
					currentLocation += commands.toToken().getStringSize();
					t.setFromIndex(currentLocation);
					commands.addToken(t);
					Token cmds = commands.toToken();
					arrays[4] = cmds;
					return new TLine(arrays);
				}
			}			
		} 
		Token error = new TSyntaxError(line, 0);
		Token[] lineResult = {error, null, null, null, null};
		return new TLine(lineResult);
	}
	
	public TRoutine tokenize(MRoutineContent content) {
		String name = content.getName();
		TRoutine result = new TRoutine(name);
		for (String line : content.getLines()) {
			try {
				TLine tokens = (TLine) this.tfLine.tokenize(line, 0);
				result.add(tokens);
			} catch (SyntaxErrorException e) {
				TLine tl = recoverFromError(line, e);
				result.add(tl);
			}
		}		
		return result;
	}
	
	public TRoutine tokenize(Path path) throws IOException,  SyntaxErrorException {
		MRoutineContent content = MRoutineContent.getInstance(path);					
		TRoutine r = this.tokenize(content);
		return r;
	}
		
	private TRoutine tokenize(String name, String line, int fromIndex) throws SyntaxErrorException {
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
		return new TFRoutine(version);
	}	
}
