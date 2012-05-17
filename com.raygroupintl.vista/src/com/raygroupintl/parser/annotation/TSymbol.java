package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.TokenFactory;

public class TSymbol extends TString implements SequencePieceGenerator {
	public TSymbol(String value) {
		super(value);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		String value = this.getStringValue();
		return map.get(value);
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
