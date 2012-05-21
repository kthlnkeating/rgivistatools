package com.raygroupintl.parser;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int code;
		
	public SyntaxErrorException(int code) {
		this.code = code;
	}
	
	public SyntaxErrorException() {
	}
	
	public int getCode() {
		return this.code;
	}
}
