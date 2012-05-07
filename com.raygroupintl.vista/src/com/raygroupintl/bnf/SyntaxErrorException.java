package com.raygroupintl.bnf;

@SuppressWarnings("serial")
public class SyntaxErrorException extends Exception {
	private int location;
	
	public SyntaxErrorException(int location) {
		this.location = location;
	}
	
	public int getLocation() {
		return this.location;
	}
}
