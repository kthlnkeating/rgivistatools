package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TCopy;
import com.raygroupintl.bnf.TList;

public class TActualList extends TCopy {
	public TActualList(TList list) {
		super(list);
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
