package com.raygroupintl.bnf;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

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
	protected final int getCodeNextIsNull(IToken[] foundTokens) {
		if (foundTokens.length == 1) {
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
	protected final int getCodeStringEnds(IToken[] foundTokens) {
		if (foundTokens.length == 1) {
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
