package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TokenFactory;

public class FSRCustom implements FactorySupplyRule {
	private TokenFactory factory;
	
	public FSRCustom(TokenFactory factory) {
		this.factory = factory;
	}
		
	@Override
	public TokenFactory getFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}

	@Override
	public boolean getRequired() {
		return true;
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
