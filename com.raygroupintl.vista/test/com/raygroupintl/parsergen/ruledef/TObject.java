package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parsergen.ruledef.TSequence;

public class TObject extends TSequence {
	public TObject(int length) {
		super(length);
	}

	public TObject(SequenceOfTokens tokens) {
		super(tokens);
	}
}
