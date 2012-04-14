package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFExprList extends TFCommaDelimitedList {
	private MVersion version;
	
	private TFExprList(MVersion version) {		
		this.version = version;
	}
	
	@Override
	protected final ITokenFactory getElementFactory() {
		return TFExpr.getInstance(this.version);
	}
	
	public static TFExprList getInstance(MVersion version) {
		return new TFExprList(version);
	}
}
