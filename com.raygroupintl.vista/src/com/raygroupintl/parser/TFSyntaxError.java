package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.ObjectSupply;

public final class TFSyntaxError extends TokenFactory {
	private int code;
	
	public TFSyntaxError(String name, int code) {
		super(name);
		this.code = code;
	}
		
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		throw new SyntaxErrorException(this.code);
	}
}
