package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class TSymbolList extends TSequence implements RuleSupply {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public FactorySupplyRule getRule(boolean required) {
		RuleSupply ers = (RuleSupply) this.get(1);
		FactorySupplyRule e = ers.getRule(true);
		TSequence delimleftright = (TSequence) this.get(2);
		if (delimleftright == null) {
			return new FSRList(e, required);
		} else {
			RuleSupply drs = (RuleSupply) delimleftright.get(1);
			FactorySupplyRule d = drs.getRule(true);
			TSequence leftright = (TSequence) delimleftright.get(2);
			if (leftright == null) {
				return new FSRDelimitedList(e, d, required);
			}
			else {
				RuleSupply lrs = (RuleSupply) leftright.get(1);
				RuleSupply rrs = (RuleSupply) leftright.get(3);
				FactorySupplyRule l = lrs.getRule(true);
				FactorySupplyRule r = rrs.getRule(true);
				return new FSREnclosedDelimitedList(e, d, l, r, required);
			}		
		}
	}
}
