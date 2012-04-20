package com.raygroupintl.bnf;


public class TFConstChar extends TFChar {
	private char constChar;
	
	public TFConstChar(char ch) {
		this.constChar = ch;
	}
		
	protected boolean isValid(char ch) {
		return ch == this.constChar;
	}
	
	public static TFConstChar getInstance(char ch) {
		return new TFConstChar(ch);
	}
}
