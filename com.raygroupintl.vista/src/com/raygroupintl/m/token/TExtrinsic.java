package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TCopy;
import com.raygroupintl.fnds.IToken;

public class TExtrinsic extends TCopy {
	public TExtrinsic(IToken source) {
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
