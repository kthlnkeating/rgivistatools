package com.raygroupintl.bnf;

public final class TFSyntaxError extends TokenFactory {
	private int code;
	
	public TFSyntaxError(int code) {
		this.code = code;
	}
		
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		throw new SyntaxErrorException(this.code);
	}
}
