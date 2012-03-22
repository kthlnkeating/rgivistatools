package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TCopy;

public class TIntrinsicVar extends TCopy {
	public TIntrinsicVar(TBasic source) {
		super(source);
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
