package com.raygroupintl.parser;

import com.raygroupintl.parser.TSequence;

public class TObject extends TSequence {
	public TObject(int length) {
		super(length);
	}

	public TObject(TokenStore store) {
		super(store.toList());
	}
}
