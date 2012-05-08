package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TFBasic;

public class TFIdent extends TFBasic {
	public TFIdent() {		
	}
	
	@Override
	protected boolean stopOn(char ch) {
		return ! Library.isIdent(ch);	
	}
	
	@Override
	protected Token getToken(String value) {
		return new TIdent(value);
	}
	
	public static TFIdent getInstance() {
		return new TFIdent();
	}
}
