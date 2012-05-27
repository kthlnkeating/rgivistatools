package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public abstract class FSRBase implements FactorySupplyRule {
	private boolean required;
	
	public FSRBase(boolean required) {
		this.required = required;
	}
	
	@Override
	public boolean getRequired() {
		return this.required;
	}

	public final TokenFactory getFactory(Map<String, TokenFactory> symbols) {
		TokenFactoriesByName fs = new TokenFactoryMap(symbols);
		return this.getFactory(fs);
	}

	public final TokenFactory getShellFactory(Map<String, TokenFactory> symbols) {
		TokenFactoriesByName fs = new TokenFactoryMap(symbols);
		return this.getShellFactory(fs);
	}
}
