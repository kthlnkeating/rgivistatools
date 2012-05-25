package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public class FSRCustom extends FSRBase {
	private TokenFactory factory;
	
	public FSRCustom(TokenFactory factory, boolean required) {
		super(required);
		this.factory = factory;
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		return this.factory;
	}

	@Override
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		return this.factory;
	}
}
