package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqRO extends TFSeqStatic {
	protected abstract ITokenFactory getRequired();
	
	protected abstract ITokenFactory getOptional();
	
	@Override
	protected ITokenFactory[] getFactories() {
		ITokenFactory r = this.getRequired();
		ITokenFactory o = this.getOptional();
		return new ITokenFactory[]{r, o};
	}

	@Override
	protected final int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		} else {		
			return RETURN_TOKEN;
		}
	}
	
	@Override
	protected final int validateEnd(int seqIndex, IToken[] foundTokens) {
		return 0;
	}
	
	public static TFSeqRO getInstance(final ITokenFactory required, final ITokenFactory optional) {
		return new TFSeqRO() {			
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
