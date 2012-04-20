package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TCopy;
import com.raygroupintl.vista.fnds.IToken;

public class TInParantheses extends TCopy {
	public TInParantheses(IToken source) {
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
