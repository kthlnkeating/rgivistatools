package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.fnds.ITokenFactory;

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
	
