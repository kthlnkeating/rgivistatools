package com.raygroupintl.parser;

import com.raygroupintl.charlib.Predicate;

public class TFChoiceOnChar0th extends TFChoiceOnChar {
	public TFChoiceOnChar0th(String name) {
		super(name);
	}

	public TFChoiceOnChar0th(String name, TokenFactory defaultFactory, Predicate[] predicates, TokenFactory... factories) {
		super(name, defaultFactory, predicates, factories);
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
