package com.raygroupintl.bnf;

public class TChar implements Token {
	private char value;
	
	public TChar(char value) {
		this.value = value;
	}
	
	@Override
	public String getStringValue() {
		return String.valueOf(this.value);
	}
	
	@Override
	public int getStringSize() {
		return 1;
	}
	
	@Override
	public void beautify() {			
	}
}
