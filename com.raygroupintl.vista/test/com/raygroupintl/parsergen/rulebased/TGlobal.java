package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TGlobal extends TSequence {
	public TGlobal(int length) {
		super(length);
	}
	
	public TGlobal(TokenStore store) {
		super(store.toList());
	}
}
