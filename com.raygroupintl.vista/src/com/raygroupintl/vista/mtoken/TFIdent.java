package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;

public class TFIdent extends TFBasic {
	@Override
	protected boolean stopOn(char ch) {
		return ! Library.isIdent(ch);	
	}
	
	protected boolean checkFirst() {
		return true;
	}
	
	@Override
	protected IToken getToken(String value) {
		return new TIdent(value);
	}
	
	public static TFIdent getInstance() {
		return new TFIdent();
	}
}
