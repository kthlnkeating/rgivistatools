package com.raygroupintl.bnf.annotation;

import java.util.Map;

import com.raygroupintl.bnf.TokenFactory;

public interface SequencePieceGenerator {
	TokenFactory getFactory(String name, Map<String, TokenFactory> map);
	boolean getRequired();
}
