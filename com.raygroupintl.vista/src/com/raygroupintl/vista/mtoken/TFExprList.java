package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFExprList extends TFCommaDelimitedList {
	private TFExprList() {		
	}
	
	@Override
	protected final ITokenFactory getElementFactory() {
		return new TFExpr();
	}
	
	public static TFExprList getInstance() {
		return new TFExprList();
	}
}
