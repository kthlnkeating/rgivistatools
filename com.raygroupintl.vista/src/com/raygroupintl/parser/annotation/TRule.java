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
	public FactorySupplyRule getRule(RuleSupplyFlag flag, String name, Map<String, RuleSupply> existing) {
		if (this.size() == 1) {
			return ((RuleSupply) this.get(0)).getRule(flag, name, existing);
		} else {
			FSRSequence result = new FSRSequence(name, flag.toRuleRequiredFlag());
			int index = 0;
			for (Token t : this) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(RuleSupplyFlag.INNER_REQUIRED, name + "." + String.valueOf(index), existing);
				if (fsr == null) return null;
				result.add(fsr);
				++index;
			}
			return result;
		}
	}
	
	public FactorySupplyRule getRule(String name) {
		return this.getRule(RuleSupplyFlag.TOP, name, new HashMap<String, RuleSupply>());
	}
}
