package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TokenFactory;

public interface TokenFactoriesByName {
	TokenFactory get(String name);
	void put(String name, TokenFactory f, FactorySupplyRule rule);
	boolean isInitialized(TokenFactory f);
}
