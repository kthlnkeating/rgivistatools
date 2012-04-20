package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public abstract class TFSeqXYY extends TFSeqStatic {
	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 1) {
			if (foundTokens[0] == null) {
				return RETURN_NULL;
			} else {
				return RETURN_TOKEN;
			}
		}
		if (seqIndex == 2) {
			return this.getErrorCode();
		}
		return CONTINUE;
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 1) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}

	public static TFSeqXYY getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqXYY() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}
}
