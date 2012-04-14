package com.raygroupintl.vista.token;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSerialRRO extends TFSerial {
	//protected IToken getTokenWhenNoOptional(IToken[] foundTokens) {
	//	return new TPair(foundTokens[0], foundTokens[1]);
	//}
	
	//protected IToken getTokenWhenAll(IToken[] foundTokens) {
	//	return new TArray(foundTokens);
	//}
	
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		}		
		if (foundTokens.length == 1) {
			return this.getErrorCode();
		}
		if (foundTokens.length == 2) {
			return RETURN_TOKEN;
		}
		return CONTINUE;
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		if (foundTokens.length == 1) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}
	
	//@Override
	//protected final IToken getToken(IToken[] foundTokens) {
	//	if (foundTokens[2] == null) {
	//		return this.getTokenWhenNoOptional(foundTokens);
	//	} else {
	//		return this.getTokenWhenAll(foundTokens);				
	//	}
	//}
	
	public static TFSerialRRO getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSerialRRO() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
