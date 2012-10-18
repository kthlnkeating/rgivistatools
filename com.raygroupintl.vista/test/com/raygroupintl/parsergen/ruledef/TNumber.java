package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TNumber extends TSequence {
	public TNumber(int length) {
		super(length);
	}

	public TNumber(Tokens store) {
		super(store.toList());
	}
}
