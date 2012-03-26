package com.raygroupintl.vista.mtoken;


import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerial;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TBasic;

public class TNumLit extends TBasic {
	private TNumLit(String value) {
		super(value);
	}
	
	private static class TFMant extends TFSerial {
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = TIntLit.getFactory();
			ITokenFactory p = new TFConstChar('.');
			return new ITokenFactory[]{f, p, f};
		}
		
		@Override
		protected final int getCodeNextIsNull(IToken[] foundTokens) {
			if (foundTokens.length == 0) {
				return RETURN_NULL;
			}		
			if (foundTokens.length == 1) {
				return RETURN_TOKEN;
			}
			if (foundTokens.length == 2) {
				return this.getErrorCode();
			}
			return CONTINUE;
		}
		
		@Override
		protected final int getCodeStringEnds(IToken[] foundTokens) {
			if (foundTokens.length == 2) {
				return this.getErrorCode();
			} else {
				return 0;
			}
		}		
	}
	
	private static class TFExp extends TFAllRequired {
		protected ITokenFactory[] getFactories() {
			ITokenFactory f = TIntLit.getFactory();
			return new ITokenFactory[]{new TFConstChar('E'), new TFConstChars("+-"), f};
		}		
	}
	
	public static class Factory extends TFSerialRO {
		@Override
		protected ITokenFactory getRequired() {
			return new TFMant();
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
	}
	
	public static ITokenFactory getFactory() {
		return new Factory();
	}
}
