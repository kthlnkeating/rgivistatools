package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TArray;

public class TGlobalNamed extends TArray {
	public TGlobalNamed(TArray spec) {
		super(spec);
		assert(spec.getCount() == 3);
	}
	
	@Override
	public String getStringValue() {
		return '^' + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return 1 + super.getStringSize();
	}	
}
