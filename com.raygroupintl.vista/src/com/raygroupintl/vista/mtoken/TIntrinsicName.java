package com.raygroupintl.vista.mtoken;

public abstract class TIntrinsicName extends TKeyword {
	public TIntrinsicName(String value) {
		super(value);
	}
	
	@Override
	public String getStringValue() {
		return "$" + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return 1 + super.getStringSize();
	}	
}
