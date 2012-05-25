package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class TSymbolList extends TSequence implements RuleSupply {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, Map<String, RuleSupply> existing) {
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = (RuleSupply) this.get(1);
		FactorySupplyRule e = ers.getRule(innerFlag, existing);
		TSequence delimleftright = (TSequence) this.get(2);
		if (e instanceof FSRChar) {
			if (delimleftright != null) {
				throw new ParseErrorException("Delimiters or enclosers are not supported with character based rules");
			}
			((FSRChar) e).setInList(true);
			return e;
		}
		boolean required = flag.toRuleRequiredFlag();
		if (delimleftright == null) {
			return new FSRList(e, required);
		} else {
			RuleSupply drs = (RuleSupply) delimleftright.get(1);
			FactorySupplyRule d = drs.getRule(RuleSupplyFlag.INNER_REQUIRED, existing);
			TSequence leftright = (TSequence) delimleftright.get(2);
			if (leftright == null) {
				return new FSRDelimitedList(e, d, required);
			}
			else {
				RuleSupply lrs = (RuleSupply) leftright.get(1);
				RuleSupply rrs = (RuleSupply) leftright.get(3);
				FactorySupplyRule l = lrs.getRule(RuleSupplyFlag.INNER_REQUIRED, existing);
				FactorySupplyRule r = rrs.getRule(RuleSupplyFlag.INNER_REQUIRED, existing);
				return new FSREnclosedDelimitedList(e, d, l, r, required);
			}		
		}
	}
}
