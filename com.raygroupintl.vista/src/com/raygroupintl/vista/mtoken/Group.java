package com.raygroupintl.vista.mtoken;


public class Group extends Multi {
	public Group(Multi tokens) {
		super(tokens);
	}
	
	@Override
	public String getStringValue() {
		return "(" + super.getStringValue() + ")";
	}
	
	@Override
	public int getStringSize() {
		return 2 + super.getStringSize();
	}	
}
