package com.raygroupintl.parser.annotation;

public interface RulesByName {
	FactorySupplyRule get(String name);
	void put(String name, FactorySupplyRule rule);
	boolean isInitialized(String name);
	
	boolean hasRule(String name);
}
