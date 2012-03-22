package com.raygroupintl.vista.mtoken;

import java.util.List;

import com.raygroupintl.vista.struct.MError;

public class TChar extends Base {
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
	public List<MError> getErrors() {
		return null;
	}

	@Override
	public void beautify() {			
	}
	
	@Override
	public boolean isError() {
		return false;
	}
}
