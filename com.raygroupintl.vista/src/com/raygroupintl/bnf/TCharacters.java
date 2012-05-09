package com.raygroupintl.bnf;

import java.util.List;

import com.raygroupintl.vista.struct.MError;

public class TCharacters extends TBase {
	private String value;
		
	public TCharacters(String value) {
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
	public List<MError> getErrors() {
		return null;
	}
	
	@Override
	public void beautify() {		
	}
}
