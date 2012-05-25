package com.raygroupintl.parser.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;

public class TRule extends TDelimitedList implements RuleSupply {
	public TRule(List<Token> token) {
		super(token);
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, Map<String, RuleSupply> existing) {
		if (this.size() == 1) {
			return ((RuleSupply) this.get(0)).getRule(flag, existing);
		} else {
			FSRSequence result = new FSRSequence(flag.toRuleRequiredFlag());
			for (Token t : this) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(RuleSupplyFlag.INNER_REQUIRED, existing);
				if (fsr == null) return null;
				result.add(fsr);
			}
			return result;
		}
	}
	
	public FactorySupplyRule getRule() {
		return this.getRule(RuleSupplyFlag.TOP, new HashMap<String, RuleSupply>());
	}
}
