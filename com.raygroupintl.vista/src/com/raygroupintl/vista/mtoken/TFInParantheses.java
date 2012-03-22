package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;

public abstract class TFInParantheses extends TFAllRequired {
	protected abstract ITokenFactory getInnerfactory();
	
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar l = new TFConstChar('(');
		TFConstChar r = new TFConstChar(')');
		ITokenFactory i = this.getInnerfactory();
		return new ITokenFactory[]{l, i, r};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {
		return new TInParantheses(foundTokens[1]);
	}
}
