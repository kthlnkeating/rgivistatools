package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TCharacters;
import com.raygroupintl.bnf.TFBasic;

public class TIntLit extends TCharacters {
	private TIntLit(String value) {
		super(value);
	}
	
	public static class Factory extends TFBasic {
		@Override
		protected boolean stopOn(char ch) {
			return (ch < '0') || (ch > '9');
		}
		
		@Override
		protected Token getToken(String value) {
			return new TIntLit(value);
		}
	}

	public static TokenFactory getFactory() {
		return new Factory();
	}
}
