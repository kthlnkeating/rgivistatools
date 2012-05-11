package com.raygroupintl.bnf;


public class TFEmpty extends TokenFactory {
	private TokenFactory expected;
	
	public TFEmpty() {		
	}
	
	public TFEmpty(TokenFactory expected) {
		this.expected = expected;
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		if (fromIndex < line.length()) {
			if (this.expected == null) {
				return new TEmpty();
			} else {
				Token t = this.expected.tokenize(line, fromIndex);
				if (t != null)  {
					return new TEmpty();
				}
			}
		}
		return null;
	}
	
	public static TFEmpty getInstance() {
		return new TFEmpty();	
	}
}
