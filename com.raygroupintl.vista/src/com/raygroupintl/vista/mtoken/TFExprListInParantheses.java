package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFExprListInParantheses extends TFInParantheses {
	@Override
	protected ITokenFactory getInnerfactory() {
		return TFExprList.getInstance();
	}
	
	public static TFExprListInParantheses getInstance() {
		return new TFExprListInParantheses();
	}
}
