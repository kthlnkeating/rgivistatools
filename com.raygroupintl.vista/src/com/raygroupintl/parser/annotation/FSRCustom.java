package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TokenFactory;

public class FSRCustom extends FSRBase {
	private TokenFactory factory;
	
	public FSRCustom(TokenFactory factory) {
		super(RuleSupplyFlag.TOP);
		this.factory = factory;
	}
		
	@Override
	public TokenFactory getFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}

	@Override
	public TokenFactory getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}

	@Override
	public String getName() {
		return this.factory.getName();
	}

	@Override
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
}
