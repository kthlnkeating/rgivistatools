package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TArray;

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
