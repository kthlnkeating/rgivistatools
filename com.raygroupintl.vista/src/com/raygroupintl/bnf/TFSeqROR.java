package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSeqROR extends TFSeqStatic {
	@Override
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 0) {
			return RETURN_NULL;
		}		
		if (foundTokens.length == 2) {
			return this.getErrorCode();
		}		
		return CONTINUE;
	}
	
	@Override
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		return this.getErrorCode();
	}
	
	public static TFSeqROR getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqROR() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
