package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFParallelCharBased;

public class TFGlvn extends TFParallelCharBased {
	@Override
	protected ITokenFactory getFactory(char ch) {
		if (Library.isIdent(ch)) {
			return new TFLvn();
		} else if (ch == '^') {
			return new TFGvn();
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
