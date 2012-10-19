package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TGlobal extends TSequence {
	public TGlobal(int length) {
		super(length);
	}
	
	public TGlobal(SequenceOfTokens tokens) {
		super(tokens);
	}
}
