package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TList;

public class TFActualList extends TFCommaDelimitedList {
	@Override
	protected ITokenFactory getElementFactory() {
		return new TFActual();
	}
	
	@Override
	protected IToken getToken(TList listToken) {
		return new TActualList(listToken);
	}
	
	public TFActualList getInstance() {
		return new TFActualList();
	}
}
