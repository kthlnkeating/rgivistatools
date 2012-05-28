package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public class TokenFactoryMap implements TokenFactoriesByName {
	private Map<String, TokenFactory> map;
	private Map<String, FactorySupplyRule> topRules;
	
	public TokenFactoryMap(Map<String, TokenFactory> map, Map<String, FactorySupplyRule> topRules) {
		this.map = map;
		this.topRules = topRules;
	}
	
	@Override
	public TokenFactory get(String name) {
		return this.map.get(name);
		//FactorySupplyRule r = this.topRules.get(name);
		//if (r != null) {
		//	r.getShellFactory(this);
		//}
		//return null;
	}
	
	@Override
	public void put(String name, TokenFactory f, FactorySupplyRule rule) {
		this.map.put(name, f);
		this.topRules.put(name, rule);
	}

	@Override
	public boolean isInitialized(TokenFactory f) {
		return f.isInitialized();
	}
}
