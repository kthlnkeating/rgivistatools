package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public interface RuleGenerator {
	TokenFactory getEmptyTokenFactory(String name);
	
	TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols);
}
