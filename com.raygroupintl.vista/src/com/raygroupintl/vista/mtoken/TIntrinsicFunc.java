package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TPair;

public class TIntrinsicFunc extends TPair {
	public TIntrinsicFunc(TBasic func, TActualList argument) {
		super(func, argument);
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
