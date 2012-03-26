package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TBasic;


public class TComment extends TBasic {
	public TComment(String value) {
		super(value);
	}
	
	@Override
	public String getStringValue() {
		return ';' + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return 1 + super.getStringSize();
	}
}

