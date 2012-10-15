package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TObject extends TSequence {
	public TObject(int length) {
		super(length);
	}

	public TObject(TokenStore store) {
		super(store.toList());
	}
}
