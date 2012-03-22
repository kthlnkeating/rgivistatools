package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFLabel extends TFParallelCharBased {
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
	
