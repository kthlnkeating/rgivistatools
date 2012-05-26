package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TokenFactory;

public class TokenFactoryMap implements TokenFactoriesByName {
	private Map<String, TokenFactory> map;
	
	public TokenFactoryMap(Map<String, TokenFactory> map) {
		this.map = map;
	}
	
	@Override
	public TokenFactory get(String name) {
		return this.map.get(name);
	}
	
	@Override
	public void put(String name, TokenFactory f) {
		this.map.put(name, f);
	}

	@Override
	public boolean isInitialized(TokenFactory f) {
		return f.isInitialized();
	}
}
