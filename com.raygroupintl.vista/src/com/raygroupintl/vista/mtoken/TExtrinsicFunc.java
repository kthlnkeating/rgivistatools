package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TPair;

public class TExtrinsicFunc extends TPair {
	public TExtrinsicFunc(TLabelRef func, TActualList argument) {
		super(func, argument);
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
