package com.raygroupintl.vista.mtoken;

import com.raygroupintl.fnds.ITokenFactory;

public class TFExprListInParantheses extends TFInParantheses {
	protected MVersion version;
	
	protected TFExprListInParantheses(MVersion version) {		
		this.version = version;
	}
		
	@Override
	protected ITokenFactory getInnerfactory() {
		return TFExprList.getInstance(this.version);
	}
	
	public static TFExprListInParantheses getInstance(MVersion version) {
		return new TFExprListInParantheses(version);
	}
}
