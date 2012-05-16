package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.Token;

public class TRequiredSymbols extends TSymbols {
	public TRequiredSymbols(Token[] tokens) {
		super(tokens);
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
