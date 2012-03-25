package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFAllOptional extends TFSerial {
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		return CONTINUE;
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return 0;
	}
	
	@Override
	protected final IToken getToken(IToken[] foundTokens) {
		for (IToken token : foundTokens) {
			if (token != null) return super.getToken(foundTokens);
		}
		return null;
	}

	public static TFAllOptional getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		return new TFAllOptional() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1};
			}
		};
	}	

	public static TFAllOptional getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFAllOptional() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
