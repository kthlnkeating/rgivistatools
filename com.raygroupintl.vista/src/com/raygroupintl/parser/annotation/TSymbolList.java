package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class TSymbolList extends TSequence implements RuleSupply {
	public TSymbolList(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		RuleSupplyFlag innerFlag = flag.demoteInner();
		RuleSupply ers = (RuleSupply) this.get(1);
		FactorySupplyRule e = ers.getRule(innerFlag, name + ".element", existing);
		if (e == null) return null;
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
			return new FSRList(name, flag, e);
		} else {
			RuleSupply drs = (RuleSupply) delimleftright.get(1);
			FactorySupplyRule d = drs.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".delimiter", existing);
			if (d == null) return null;
			TSequence leftright = (TSequence) delimleftright.get(2);
			if (leftright == null) {
				return new FSRDelimitedList(name, flag, e, d);
			}
			else {
				RuleSupply lrs = (RuleSupply) leftright.get(1);
				RuleSupply rrs = (RuleSupply) leftright.get(3);
				Token emptyAllowedToken = leftright.get(5);
				Token noneAllowedToken = leftright.get(7);
				
				FactorySupplyRule l = lrs.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".left", existing);
				FactorySupplyRule r = rrs.getRule(RuleSupplyFlag.INNER_REQUIRED, name + ".right", existing);
				if ((l == null) || (r == null)) return null;
				FSREnclosedDelimitedList result = new FSREnclosedDelimitedList(name, flag, e, d, l, r);
				if ((emptyAllowedToken != null) && (emptyAllowedToken.getStringValue().equals("1"))) result.setEmptyAllowed(true);
				if ((noneAllowedToken != null) && (noneAllowedToken.getStringValue().equals("1"))) result.setNoneAllowed(true);
				return result;
			}		
		}
	}
}
