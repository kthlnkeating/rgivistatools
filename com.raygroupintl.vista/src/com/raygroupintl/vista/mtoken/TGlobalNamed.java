package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TArray;

public class TGlobalNamed extends TArray {
	public TGlobalNamed(IToken[] spec) {
		super(spec);
		assert(spec.length == 3);
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
