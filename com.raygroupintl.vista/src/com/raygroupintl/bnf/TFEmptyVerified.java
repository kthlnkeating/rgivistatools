package com.raygroupintl.bnf;


public abstract class TFEmptyVerified implements TokenFactory {
	protected abstract boolean isExpected(char ch);
		
	@Override
	public Token tokenize(String line, int fromIndex) {
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
	
	public static TFEmptyVerified getInstance(final char chIn) {
		return new TFEmptyVerified() {			
			@Override
			protected boolean isExpected(char ch) {
				return chIn == ch;
			}
		};
	}
}
