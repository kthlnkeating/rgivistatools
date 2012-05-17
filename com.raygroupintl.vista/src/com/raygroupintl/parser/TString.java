package com.raygroupintl.parser;

public class TString implements Token {
	private String value;
		
	public TString(String value) {
		this.value = value;
	}
	
	protected String getValue() {
		return this.value;
	}
	
	@Override
	public String getStringValue() {
		return this.value;
	}
	
	@Override
	public int getStringSize() {
		return this.value.length();
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void beautify() {		
	}
}
