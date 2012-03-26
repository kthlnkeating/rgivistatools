package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TArray;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFSerialRO;

public class TFIntrinsic extends TFSerialRO {
	private static class TFIntrinsicHead extends TFAllRequired {
		@Override
		protected ITokenFactory[] getFactories() {
			return new ITokenFactory[]{TFConstChar.getInstance('$'), TFIdent.getInstance()};
		}
	}
	
	@Override
	protected ITokenFactory getRequired() {
		return new TFIntrinsicHead();
	}

	@Override
	protected ITokenFactory getOptional() {
		return new TFActualList();
	}

	private TIdent toTIdent(IToken token) {
		return (TIdent) ((TArray) token).get(1);	
	}
	
	protected IToken getTokenRequired(IToken requiredToken) {
		return TIntrinsicVar.getInstance(this.toTIdent(requiredToken)) ;
	}
	
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		return TIntrinsicFunc.getInstance(this.toTIdent(requiredToken), (TActualList) optionalToken);
	}
	
	public static TFIntrinsic getInstance() {
		return new TFIntrinsic();
	}
}
