package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TCharacters;


public class TComment extends TCharacters {
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

