package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialORO;

public class TFGvn extends TFAllRequired {
	private MVersion version;
	
	private TFGvn(MVersion version) {
		this.version = version;
	}
	
	@Override
	protected ITokenFactory[] getFactories() {
		TFConstChar c = TFConstChar.getInstance('^');
		TFEnvironment env = TFEnvironment.getInstance(this.version);
		TFName name = new TFName();
		TFExprListInParantheses exprList = TFExprListInParantheses.getInstance(this.version);
		ITokenFactory r = TFSerialORO.getInstance(env, name, exprList);
		return new ITokenFactory[]{c, r};
	}

	@Override
	protected IToken getToken(IToken[] foundTokens) {			
		return new TGlobalNamed((TArray) foundTokens[1]);
	}		

	public static TFGvn getInstance(MVersion version) {
		return new TFGvn(version);
	}
}

