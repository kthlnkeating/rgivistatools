package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parsergen.ruledef.TTSequence;

public class TTLocal extends TTSequence {
	public TTLocal(int length) {
		super(length);
	}
	
	public TTLocal(SequenceOfTokens<Token> tokens) {
		super(tokens);
	}
}
