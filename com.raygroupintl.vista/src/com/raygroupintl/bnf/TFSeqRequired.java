package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSeqRequired extends TFSeqStatic {
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
	
	public static TFSeqRequired getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		return new TFSeqRequired() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1};
			}
		};
	}	

	public static TFSeqRequired getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqRequired() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
