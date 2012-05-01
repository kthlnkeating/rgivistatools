package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TBasic;


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

