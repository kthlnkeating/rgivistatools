package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFChoice;

public class TFLabel extends TFChoice {
	protected ITokenFactory getFactory(char ch) {
		if (Library.isIdent(ch) || (ch == '%')) {
			return new TFName();
		} else if (Library.isDigit(ch)) {
			return new TIntLit.Factory();
		} else {
			return null;
		}
	}
	
	public static TFLabel getInstance() {
		return new TFLabel();
	}
}
	
