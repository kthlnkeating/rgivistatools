package com.raygroupintl.bnf;


public class TFEmpty extends TokenFactory {
	private TokenFactory expected;
	
	public TFEmpty() {		
	}
	
	public TFEmpty(TokenFactory expected) {
		this.expected = expected;
	}
	
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		if (text.onChar()) {
			if (this.expected == null) {
				return new TEmpty();
			} else {
				Text textCopy = text.getCopy();
				Token t = this.expected.tokenize(textCopy);
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
