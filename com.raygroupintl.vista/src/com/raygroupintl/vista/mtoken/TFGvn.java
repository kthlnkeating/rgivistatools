package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialORO;

public class TFGvn extends TFAllRequired {
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar c = TFConstChar.getInstance('^');
		TFEnvironment env = new TFEnvironment();
		TFName name = new TFName();
		TFExprListInParantheses exprList = new TFExprListInParantheses();
		ITokenFactory r = TFSerialORO.getInstance(env, name, exprList);
		return new ITokenFactory[]{c, r};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {			
		return new TGlobalNamed((TArray) foundTokens[1]);
	}		

	public static TFGvn getInstance() {
		return new TFGvn();
	}
}

