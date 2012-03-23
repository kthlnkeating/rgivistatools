package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TCopy;

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
