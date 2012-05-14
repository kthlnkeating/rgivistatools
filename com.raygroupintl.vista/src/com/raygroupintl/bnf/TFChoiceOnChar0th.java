package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class TFChoiceOnChar0th extends TFChoiceOnChar {
	public TFChoiceOnChar0th() {
		super();
	}

	public TFChoiceOnChar0th(TokenFactory defaultFactory, Predicate[] predicates, TokenFactory... factories) {
		super(defaultFactory, predicates, factories);
	}

	@Override
	protected TokenFactory getFactory(Text text) {
		if (text.onChar()) {
			char ch = text.getChar();
			return this.getFactory(ch);
		} else {
			return null;
		}
	}		
}
