package com.raygroupintl.bnf;

import java.util.List;

@SuppressWarnings("serial")
public class SyntaxErrorInListException extends SyntaxErrorException {
	private List<Token> previousTokens;
	
	public SyntaxErrorInListException(SyntaxErrorException elementError, List<Token> previousTokens) {
		super(elementError.getCode(), elementError.getLocation());
		this.previousTokens = previousTokens;
	}
	
	public List<Token> getPreviousTokens() {
		return this.previousTokens;
	}
}
