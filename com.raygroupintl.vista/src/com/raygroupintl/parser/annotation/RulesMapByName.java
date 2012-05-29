package com.raygroupintl.parser.annotation;

import java.util.Map;

public class RulesMapByName implements RulesByName {
	private Map<String, FactorySupplyRule> topRules;
	
	public RulesMapByName(Map<String, FactorySupplyRule> topRules) {
		this.topRules = topRules;
	}
	
	@Override
	public FactorySupplyRule get(String name) {
		return this.topRules.get(name);
	}
	
	@Override
	public void put(String name, FactorySupplyRule rule) {
		this.topRules.put(name, rule);
	}

	@Override
	public boolean hasRule(String name) {
		return this.topRules.get(name) != null;
	}
}
