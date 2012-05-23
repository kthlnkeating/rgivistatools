package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public interface TopTFRule {
	TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell);
}
