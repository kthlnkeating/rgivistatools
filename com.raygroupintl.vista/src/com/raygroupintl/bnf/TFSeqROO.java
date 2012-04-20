package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSeqROO extends TFSeqStatic {
	protected IToken getTokenWhenNoOptional(IToken token) {
		return token;
	}
	
	protected IToken getTokenWhenAnyOptional(IToken[] foundTokens) {
		return new TArray(foundTokens);
	}
	
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		}		
		return CONTINUE;
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return 0;
	}
	
	@Override
	protected final IToken getToken(IToken[] foundTokens) {
		if ((foundTokens[1] == null) && (foundTokens[2] == null)) {
			return this.getTokenWhenNoOptional(foundTokens[0]);
		} else {
			return this.getTokenWhenAnyOptional(foundTokens);				
		}
	}
	
	public static TFSeqROO getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqROO() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
