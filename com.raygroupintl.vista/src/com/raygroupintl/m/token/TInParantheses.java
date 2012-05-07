package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TCopy;

public class TInParantheses extends TCopy {
	public TInParantheses(Token source) {
		super(source);
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
