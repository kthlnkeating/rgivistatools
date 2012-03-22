package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public class TFIdent extends TFBasic {
	@Override
	protected boolean stopOn(char ch) {
		return ! Library.isIdent(ch);	
	}
	
	@Override
	protected IToken getToken(String value) {
		return new TIdent(value);
	}
}
