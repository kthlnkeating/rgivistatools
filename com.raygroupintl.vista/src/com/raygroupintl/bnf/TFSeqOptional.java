package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class TFSeqOptional extends TFSeqStatic {
	@Override
	protected final IToken getToken(IToken[] foundTokens) {
		for (IToken token : foundTokens) {
			if (token != null) return super.getToken(foundTokens);
		}
		return null;
	}

	public static TFSeqOptional getInstance(final ITokenFactory f0, final ITokenFactory f1) {
		return new TFSeqOptional() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1};
			}
		};
	}	

	public static TFSeqOptional getInstance(final ITokenFactory f0, final ITokenFactory f1, final ITokenFactory f2) {
		return new TFSeqOptional() {			
			@Override
			protected ITokenFactory[] getFactories() {
				return new ITokenFactory[]{f0, f1, f2};
			}
		};
	}	
}
