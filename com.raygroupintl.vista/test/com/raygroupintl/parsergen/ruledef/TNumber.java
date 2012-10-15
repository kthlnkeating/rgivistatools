package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TNumber extends TSequence {
	public TNumber(int length) {
		super(length);
	}

	public TNumber(TokenStore store) {
		super(store.toList());
	}
}
