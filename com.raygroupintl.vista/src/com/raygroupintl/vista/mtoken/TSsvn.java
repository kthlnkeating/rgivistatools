package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.token.TArray;

public class TSsvn extends TArray {
	public TSsvn(IToken ssvn, IToken param) {
		super(new IToken[]{ssvn, param});
	}
	
	@Override
	public String getStringValue() {
		return "^$" + super.getStringValue();
	}

	@Override
	public int getStringSize() {
		return 2 + super.getStringSize();
	}	
}
