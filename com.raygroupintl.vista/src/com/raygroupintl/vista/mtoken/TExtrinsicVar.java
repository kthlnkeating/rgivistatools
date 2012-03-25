package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TCopy;

public class TExtrinsicVar extends TCopy {
	public TExtrinsicVar(TLabelRef source) {
		super(source);
	}
	
	@Override
	public String getStringValue() {
		return "$$" + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return 2 + super.getStringSize();
	}	
}