package com.raygroupintl.bnf;


public abstract class TFEmptyVerified extends TokenFactory {
	protected abstract boolean isExpected(char ch);
		
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		if (text.onChar()) {
			char ch = text.getChar();
			if (this.isExpected(ch)) {
				return new TEmpty();
			} else {
				throw new SyntaxErrorException();
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
