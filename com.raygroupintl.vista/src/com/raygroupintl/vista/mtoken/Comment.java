package com.raygroupintl.vista.mtoken;


public class Comment extends TBasic {
	public Comment(String value) {
		super(value);
	}
	
	@Override
	public String getStringValue() {
		return ';' + super.getStringValue();
	}
}

