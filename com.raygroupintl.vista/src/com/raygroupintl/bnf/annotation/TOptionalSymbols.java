package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.Token;

public class TOptionalSymbols extends TSymbols {
	public TOptionalSymbols(Token[] tokens) {
		super(convertEnclosed(tokens));
	}

	@Override	
	public boolean getRequired() {
		return false;
	}	
}
