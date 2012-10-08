package com.raygroupintl.parser;

import com.raygroupintl.parser.TSequence;

public class TNumber extends TSequence {
	public TNumber(int length) {
		super(length);
	}

	public TNumber(TokenStore store) {
		super(store.toList());
	}
}
