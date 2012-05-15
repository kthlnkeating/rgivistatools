package com.raygroupintl.bnf;

public final class TFSyntaxError extends TokenFactory {
	private int code;
	
	public TFSyntaxError(String name, int code) {
		super(name);
		this.code = code;
	}
		
	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		throw new SyntaxErrorException(this.code);
	}
}
