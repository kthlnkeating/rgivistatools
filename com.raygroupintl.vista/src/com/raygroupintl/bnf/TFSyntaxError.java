package com.raygroupintl.bnf;

public final class TFSyntaxError implements TokenFactory {
	private int code;
	
	public TFSyntaxError(int code) {
		this.code = code;
	}
		
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		throw new SyntaxErrorException(this.code, fromIndex);
	}
}
