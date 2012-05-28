package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

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
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
	
	@Override
	public TokenFactory getFactory(RulesByName symbols) {
		FactorySupplyRule f = symbols.get(this.value);
		if (f == null) {
			throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		}
		TokenFactory result = f.getShellFactory();
		if (! symbols.isInitialized(this.value)) {
			return null;
		}
		return result;
	}

	@Override
	public TFBasic getShellFactory() {
		throw new ParseErrorException("Not a top rule.");
	}
}