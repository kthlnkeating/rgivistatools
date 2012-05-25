package com.raygroupintl.parser.annotation;

import java.util.List;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;

public class TRule extends TDelimitedList implements RuleSupply {
	public TRule(List<Token> token) {
		super(token);
	}
	
	@Override
	public FactorySupplyRule getRule(boolean required) {
		if (this.size() == 1) {
			return ((RuleSupply) this.get(0)).getRule(required);
		} else {
			FSRSequence result = new FSRSequence(required);
			for (Token t : this) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(true);
				result.add(fsr);
			}
			return result;
		}
	}
}
