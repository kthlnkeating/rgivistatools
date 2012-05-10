package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TCopy;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;

public class TActualList extends TCopy {
	public TActualList(TList list) {
		super(list);
	}
	
	public TActualList(Token[] tokens) {
		super((tokens[1] == null) ? new TList() : (TList) tokens[1]);
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
