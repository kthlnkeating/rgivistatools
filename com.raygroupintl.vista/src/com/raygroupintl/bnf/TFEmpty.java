package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFEmpty implements ITokenFactory {
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			return new TEmpty();
		}
		return null;
	}
	
	public static TFEmpty getInstance() {
		return new TFEmpty();	
	}
}
