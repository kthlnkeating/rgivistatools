package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;

public class TFEnvironment extends TFAllRequired {
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar l = new TFConstChar('|');
		TFConstChar r = new TFConstChar('|');
		ITokenFactory i = new TFExpr();
		return new ITokenFactory[]{l, i, r};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TEnvironment(foundTokens[1]);
	}
	
	public static TFEnvironment getInstance() {
		return new TFEnvironment();
	}
}
