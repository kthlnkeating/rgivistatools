package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TGlobal extends TSequence {
	public TGlobal(int length) {
		super(length);
	}
	
	public TGlobal(Tokens store) {
		super(store.toList());
	}
}
