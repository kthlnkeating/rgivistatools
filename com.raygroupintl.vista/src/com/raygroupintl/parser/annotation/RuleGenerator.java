package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public interface RuleGenerator {
	TFBasic getTopFactoryShell(String name, Map<String, TokenFactory> symbols);
	
	TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols);
}
