package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFGlvn extends TFParallelCharBased {
	@Override
	protected ITokenFactory getFactory(char ch) {
		if ((ch == '%') || Library.isIdent(ch)) {
			return new TFLvn();
		} else if (ch == '^') {
			return new TFGvnAll();
		} else if (ch == '@') {
			return new TFIndirection();
		} else {
			return null;
		}		
	}
	
	public static TFGlvn getInstance() {
		return new TFGlvn();
	}
}
