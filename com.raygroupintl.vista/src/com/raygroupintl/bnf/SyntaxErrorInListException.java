package com.raygroupintl.bnf;

@SuppressWarnings("serial")
public class SyntaxErrorInListException extends SyntaxErrorException {
	private TokenStore previousTokens;
	
	public SyntaxErrorInListException(SyntaxErrorException elementError, TokenStore previousTokens) {
		super(elementError.getCode(), elementError.getLocation());
		this.previousTokens = previousTokens;
	}
	
	public TokenStore getPreviousTokens() {
		return this.previousTokens;
	}
}
