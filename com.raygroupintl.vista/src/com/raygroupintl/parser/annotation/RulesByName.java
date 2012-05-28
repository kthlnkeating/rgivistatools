package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.OrderedNameContainer;

public interface RulesByName extends OrderedNameContainer {
	FactorySupplyRule get(String name);
	void put(String name, FactorySupplyRule rule);
	boolean isInitialized(String name);
	
	boolean hasRule(String name);
}
