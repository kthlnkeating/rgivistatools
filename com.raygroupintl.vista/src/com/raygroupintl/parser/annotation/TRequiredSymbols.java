package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.Token;

public class TRequiredSymbols extends TSymbols {
	public TRequiredSymbols(java.util.List<Token> tokens) {
		super(tokens, true);
	}
}
