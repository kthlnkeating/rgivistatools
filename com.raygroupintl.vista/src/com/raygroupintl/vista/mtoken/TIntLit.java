package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TBasic;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TIntLit extends TBasic {
	private TIntLit(String value) {
		super(value);
	}
	
	public static class Factory extends TFBasic {
		@Override
		protected boolean stopOn(char ch) {
			return (ch < '0') || (ch > '9');
		}
		
		@Override
		protected IToken getToken(String value) {
			return new TIntLit(value);
		}
	}

	public static ITokenFactory getFactory() {
		return new Factory();
	}
}
