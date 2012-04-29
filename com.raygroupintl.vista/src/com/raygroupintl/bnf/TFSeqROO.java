package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public class TFSeqROO extends TFSeqStatic {
	public TFSeqROO() {
		boolean[] flags = {true, false, false};
		this.setRequiredFlags(flags);		
	}
	
	public TFSeqROO(ITokenFactory... factories) {
		super(factories);
		boolean[] flags = {true, false, false};
		this.setRequiredFlags(flags);
	}
	
	protected IToken getTokenWhenNoOptional(IToken token) {
		return token;
	}
	
	protected IToken getTokenWhenAnyOptional(IToken[] foundTokens) {
		return new TArray(foundTokens);
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
		return new TFSeqROO(f0, f1, f2); 
	}
}
