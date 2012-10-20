package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TLocal extends TSequence {
	public TLocal(int length) {
		super(length);
	}
	
	public TLocal(SequenceOfTokens tokens) {
		super(tokens);
	}
}