package com.raygroupintl.vista.mtoken;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFSeqORO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFGvn extends TFSeqRequired {
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
		ITokenFactory r = TFSeqORO.getInstance(env, name, exprList);
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

