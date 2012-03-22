package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSerialRO extends TFSerial {
	protected abstract ITokenFactory getRequired();
	
	protected abstract ITokenFactory getOptional();
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory r = this.getRequired();
		ITokenFactory o = this.getOptional();
		return new ITokenFactory[]{r, o};
	}

	protected IToken getTokenRequired(IToken requiredToken) {
		return requiredToken;
	}
	
	protected IToken getTokenBoth(IToken requiredToken, IToken optionalToken) {
		return new TPair(requiredToken, optionalToken);
	}
	
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		} else {		
			return RETURN_TOKEN;
		}
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return 0;
	}
	
	@Override
	protected final IToken getToken(IToken[] foundTokens) {
		if (foundTokens[1] == null) {
			return this.getTokenRequired(foundTokens[0]);
		} else {
			return this.getTokenBoth(foundTokens[0], foundTokens[1]);				
		}
	}
	
	public static TFSerialRO getInstance(final ITokenFactory required, final ITokenFactory optional) {
		return new TFSerialRO() {			
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
