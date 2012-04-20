package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqOR extends TFSeqStatic {
	protected abstract ITokenFactory getRequired();
	
	protected abstract ITokenFactory getOptional();
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory r = this.getRequired();
		ITokenFactory o = this.getOptional();
		return new ITokenFactory[]{o, r};
	}

	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 1) {
			if (foundTokens[0] == null) {
				return RETURN_NULL;
			} else {
				return this.getErrorCode();
			}
		} else {		
			return CONTINUE;
		}
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return this.getErrorCode();
		} else {
			return 0;
		}
	}
	
	public static TFSeqOR getInstance(final ITokenFactory optional, final ITokenFactory required) {
		return new TFSeqOR() {			
			@Override
			protected ITokenFactory getRequired() {
				return required;
			}
			
			@Override
			protected ITokenFactory getOptional() {
				return optional;
			}
		};			
	}	
}
