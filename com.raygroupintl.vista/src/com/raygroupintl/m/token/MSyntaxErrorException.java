package com.raygroupintl.m.token;

import com.raygroupintl.bnf.SyntaxErrorException;

@SuppressWarnings("serial")
public class MSyntaxErrorException extends SyntaxErrorException {
	public MSyntaxErrorException(int code, int location) {
		super(code, location);
	}
}
