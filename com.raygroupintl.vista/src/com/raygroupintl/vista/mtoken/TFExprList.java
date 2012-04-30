package com.raygroupintl.vista.mtoken;

import com.raygroupintl.fnds.ITokenFactory;

public class TFExprList extends TFCommaDelimitedList {
	private MVersion version;
	
	private TFExprList(MVersion version) {		
		this.version = version;
	}
	
	@Override
	protected final ITokenFactory getElementFactory() {
		return MTFSupply.getInstance(version).getTFExpr();
	}
	
	public static TFExprList getInstance(MVersion version) {
		return new TFExprList(version);
	}
}
