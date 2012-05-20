package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public interface RuleGenerator {
	TokenFactory getTopFactoryShell(String name, Map<String, TokenFactory> symbols);
	
	TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols);
}
