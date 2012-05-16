package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.Token;

public class TOptionalSymbols extends TSymbols {
	public TOptionalSymbols(java.util.List<Token> tokens) {
		super(tokens);
	}

	@Override	
	public boolean getRequired() {
		return false;
	}	
}
