package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqROO extends TFSeqStatic {
	protected IToken getTokenWhenNoOptional(IToken token) {
		return token;
	}
	
	protected IToken getTokenWhenAnyOptional(IToken[] foundTokens) {
		return new TArray(foundTokens);
	}
	
	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}		
		return CONTINUE;
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
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
