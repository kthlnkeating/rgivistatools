package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TLocal extends TSequence {
	public TLocal(int length) {
		super(length);
	}
	
	public TLocal(TokenStore store) {
		super(store.toList());
	}
}
