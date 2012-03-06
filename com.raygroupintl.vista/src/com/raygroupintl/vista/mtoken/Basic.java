package com.raygroupintl.vista.mtoken;


public class Basic extends Base {
	private String value;
		
	public Basic(String value) {
		this.value = value;
	}
		
	@Override
	public String getStringValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void beautify() {		
	}
}
