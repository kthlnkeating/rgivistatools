package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSerialOR extends TFSerial {
	protected abstract ITokenFactory getRequired();
	
	protected abstract ITokenFactory getOptional();
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory r = this.getRequired();
		ITokenFactory o = this.getOptional();
		return new ITokenFactory[]{o, r};
	}

	protected IToken getTokenRequired(IToken requiredToken) {
		return requiredToken;
	}
	
	protected IToken getTokenBoth(IToken optionalToken, IToken requiredToken) {
		return new TPair(optionalToken, requiredToken);
	}
	
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 1) {
			if (foundTokens[0] == null) {
				return RETURN_NULL;
			} else {
				return this.getErrorCode();
			}
		} else {		
			return CONTINUE;
		}
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		if (foundTokens.length == 1) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}
	
	@Override
	protected final IToken getToken(IToken[] foundTokens) {
		if (foundTokens[0] == null) {
			return this.getTokenRequired(foundTokens[1]);
		} else {
			return this.getTokenBoth(foundTokens[0], foundTokens[1]);				
		}
	}
	
	public static TFSerialOR getInstance(final ITokenFactory optional, final ITokenFactory required) {
		return new TFSerialOR() {			
			@Override
			protected ITokenFactory getRequired() {
				return required;
			}
			
			@Override
			protected ITokenFactory getOptional() {
				return optional;
			}
		};			
	}	
}
