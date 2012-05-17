package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public interface SequencePieceGenerator {
	TokenFactory getFactory(String name, Map<String, TokenFactory> map);
	boolean getRequired();
}
