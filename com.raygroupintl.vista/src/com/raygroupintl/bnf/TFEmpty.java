package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFEmpty implements ITokenFactory {
	protected abstract boolean isExpected(char ch);
		
	@Override
	public IToken tokenize(String line, int fromIndex) {
		if (fromIndex < line.length()) {
			char ch = line.charAt(fromIndex);
			if (this.isExpected(ch)) {
				return new TEmpty();
			} else {
				return new TSyntaxError(line, fromIndex);
			}
		}
		return null;
	}
	
	public static TFEmpty getInstance(final char chIn) {
		return new TFEmpty() {			
			@Override
			protected boolean isExpected(char ch) {
				return chIn == ch;
			}
		};
	}

	public static TFEmpty getInstance() {
		return getInstance(' ');
	}
}
