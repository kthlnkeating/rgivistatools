package com.raygroupintl.parser.annotation;

public interface RulesByName  {
	FactorySupplyRule get(String name);
	void put(String name, FactorySupplyRule rule);
	
	boolean hasRule(String name);
}
