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
		if (list.size() == 1) {
			return ((RuleSupply) list.get(0)).getRule(this.required);
		} else {		
			FSRSequence result = new FSRSequence(this.required);
			for (Token t : list) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(true);
				result.add(fsr);
			}
			return result;
		}
	}
}
