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
