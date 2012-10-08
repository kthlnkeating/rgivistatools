package com.raygroupintl.parser;

import com.raygroupintl.parser.TSequence;

public class TGlobal extends TSequence {
	public TGlobal(int length) {
		super(length);
	}
	
	public TGlobal(TokenStore store) {
		super(store.toList());
	}
}
