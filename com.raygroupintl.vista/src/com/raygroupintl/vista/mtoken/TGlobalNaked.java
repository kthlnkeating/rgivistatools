package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TCopy;
import com.raygroupintl.fnds.IToken;

public class TGlobalNaked extends TCopy {
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
