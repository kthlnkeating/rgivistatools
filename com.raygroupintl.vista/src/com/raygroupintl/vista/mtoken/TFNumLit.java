package com.raygroupintl.vista.mtoken;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFSerialOR;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialROR;
import com.raygroupintl.vista.token.TFSerialXYY;

public class TFNumLit extends TFSerialRO {
	private static class TFExp extends TFSerialROR {
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = TIntLit.getFactory();
			return new ITokenFactory[]{new TFConstChar('E'), new TFConstChars("+-"), f};
		}		
	}
	
	@Override
	protected ITokenFactory getRequired() {
		ITokenFactory f = TIntLit.getFactory();
		ITokenFactory p = new TFConstChar('.');
		TFSerialXYY number = TFSerialXYY.getInstance(f, p, f);
		return TFSerialOR.getInstance(TFConstChars.getInstance("-+"), number);
	}
	
	@Override
	protected ITokenFactory getOptional() {
		return new TFExp();
	}
	
	@Override
	protected IToken getTokenRequired(IToken requiredToken) {
		String value = requiredToken.getStringValue();
		return new TNumLit(value);
	}
	
	@Override
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		String value = requiredToken.getStringValue() + optionalToken.getStringValue();
		return new TNumLit(value);
	}
	
	public static TFNumLit getInstance() {
		return new TFNumLit();
	}
}
