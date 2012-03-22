package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public class TGlobalNaked extends TInParantheses {
	public TGlobalNaked(IToken source) {
		super(source);
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
