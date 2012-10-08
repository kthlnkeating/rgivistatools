package com.raygroupintl.parser;

import com.raygroupintl.parser.TSequence;

public class TLocal extends TSequence {
	public TLocal(int length) {
		super(length);
	}
	
	public TLocal(TokenStore store) {
		super(store.toList());
	}
}
