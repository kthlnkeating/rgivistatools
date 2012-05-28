package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.OrderedName;

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
	public boolean isInitialized(String name) {
		FactorySupplyRule r = this.get(name);
		return (r != null) && (r.getShellFactory().isInitialized());
	}

	@Override
	public boolean hasRule(String name) {
		return this.topRules.get(name) != null;
	}
	
	public boolean hasName(String name) {
		return this.hasRule(name);
	}
	@Override
	public OrderedName getNamed(String name) {
		return this.get(name);
	}
}
