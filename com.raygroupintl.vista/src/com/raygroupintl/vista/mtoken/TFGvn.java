package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFSerialORO;

public class TFGvn extends TFSerialORO {
	@Override
	protected ITokenFactory[] getFactories() {			
		TFEnvironment env = new TFEnvironment();
		TFName name = new TFName();
		TFExprListInParantheses exprList = new TFExprListInParantheses();
		return new ITokenFactory[]{env, name, exprList};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {			
		return new TGlobalNamed(foundTokens);
	}		

	public static TFGvn getInstance() {
		return new TFGvn();
	}
}

