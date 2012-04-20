package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;

public abstract class TFSeqORR extends TFSeqStatic {
	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 1) {
			if (foundTokens[0] == null) {
				return RETURN_NULL;
			} else {
				return this.getErrorCode();
			}
		}
		if (seqIndex == 2) {
			return this.getErrorCode();
		}
		return CONTINUE;
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex < 2) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}
}
