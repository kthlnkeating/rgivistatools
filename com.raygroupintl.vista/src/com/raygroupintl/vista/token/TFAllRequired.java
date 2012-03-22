package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFAllRequired extends TFSerial {
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		} else {
			return this.getErrorCode();
		}
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return this.getErrorCode();
	}
	
	public static TFAllRequired getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		return new TFAllRequired() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1};
			}
		};
	}	

	public static TFAllRequired getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFAllRequired() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
