package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.Token;

public class TRequiredSymbols extends TSymbols {
	public TRequiredSymbols(Token token) {
		super(token, true);
	}
}
