package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;

public class TFTimeout extends TFAllRequired {
	@Override
	protected ITokenFactory[] getFactories() {
		return new ITokenFactory[]{TFConstChar.getInstance(':'), TFExpr.getInstance()};
	}
	
	public static TFTimeout getInstance() {
		return new TFTimeout();
	}
}
