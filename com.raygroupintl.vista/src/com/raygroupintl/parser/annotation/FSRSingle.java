package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.OrderedName;
import com.raygroupintl.parser.OrderedNameContainer;
import com.raygroupintl.parser.TFBasic;

public class FSRSingle extends FSRBase {
	private String value;
		
	public FSRSingle(String value, RuleSupplyFlag flag) {
		super(flag);
		this.value = value;
	}
	
	@Override
	public String getName() {
		return this.value;
	}
	
	@Override
	public OrderedName getLeading(OrderedNameContainer names) {
		return names.getNamed(this.value);
	}	
	
	@Override
	public FactorySupplyRule getActualRule(RulesByName symbols) {
		FactorySupplyRule f = symbols.get(this.value);
		if (f == null) {
			throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		}
		return f;
	}
	
	@Override
	public TFBasic getShellFactory() {
		throw new ParseErrorException("Not a top rule.");
	}
}