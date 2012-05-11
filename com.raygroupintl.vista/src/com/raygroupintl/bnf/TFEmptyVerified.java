package com.raygroupintl.bnf;


public abstract class TFEmptyVerified extends TokenFactory {
	protected abstract boolean isExpected(char ch);
		
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		if (fromIndex < line.length()) {
			char ch = line.charAt(fromIndex);
			if (this.isExpected(ch)) {
				return new TEmpty();
			} else {
				throw new SyntaxErrorException(fromIndex);
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
