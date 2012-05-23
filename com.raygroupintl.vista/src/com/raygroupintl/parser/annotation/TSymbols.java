package com.raygroupintl.parser.annotation;

import java.util.List;

import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public abstract class TSymbols extends TSequence implements RuleSupply {
	private boolean required;
	
	public TSymbols(List<Token> tokens, boolean required) {
		super(tokens);
		this.required = required;
	}

	@Override
	public FactorySupplyRule getRule(boolean required) {
		TList list = (TList) this.get(1);

		Token lastToken = null;
		int count = 0;
		for (Token t : list) {
			lastToken = t;
			++count;
		}
		
		if (count == 1) {
			return ((RuleSupply) lastToken).getRule(required);
		}
		
		FSRSequence result = new FSRSequence(required);
		for (Token t : list) {
			RuleSupply rs = (RuleSupply) t;
			FactorySupplyRule fsr = rs.getRule(this.required);
			result.add(fsr);
		}
		return result;
	}
}
