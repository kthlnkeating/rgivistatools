package com.raygroupintl.parser.annotation;

import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public abstract class TSymbols extends TSequence implements RuleSupply {
	private boolean required;
	
	public TSymbols(List<Token> tokens, boolean required) {
		super(tokens);
		this.required = required;
	}

	private RuleSupplyFlag convert(RuleSupplyFlag flag) {
		switch (flag) {
			case INNER_OPTIONAL:
			case INNER_REQUIRED:
				return this.required ? RuleSupplyFlag.INNER_REQUIRED : RuleSupplyFlag.INNER_OPTIONAL;
			default:
				return flag;
		}
	}
	
	@Override
	public FactorySupplyRule getRule(RuleSupplyFlag flag, Map<String, RuleSupply> existing) {
		TList list = (TList) this.get(1);
		RuleSupplyFlag newFlag = this.convert(flag);
		if (list.size() == 1) {
			return ((RuleSupply) list.get(0)).getRule(newFlag, existing);
		} else {		
			FSRSequence result = new FSRSequence(this.required);
			for (Token t : list) {
				RuleSupply rs = (RuleSupply) t;
				FactorySupplyRule fsr = rs.getRule(RuleSupplyFlag.INNER_REQUIRED, existing);
				result.add(fsr);
			}
			return result;
		}
	}
}
