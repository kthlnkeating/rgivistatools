package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqRRO extends TFSeqStatic {
	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}		
		if (seqIndex == 1) {
			return this.getErrorCode();
		}
		if (seqIndex == 2) {
			return RETURN_TOKEN;
		}
		return CONTINUE;
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}
	
	public static TFSeqRRO getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqRRO() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
