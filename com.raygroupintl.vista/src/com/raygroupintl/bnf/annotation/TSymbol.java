package com.raygroupintl.bnf.annotation;

import java.util.Map;

import com.raygroupintl.bnf.TCharacters;
import com.raygroupintl.bnf.TokenFactory;

public class TSymbol extends TCharacters implements SequencePieceGenerator{
	public TSymbol(String value) {
		super(value);
	}
	
	@Override
	public TokenFactory getFactory(Map<String, TokenFactory> map) {
		String value = this.getStringValue();
		return map.get(value);
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
