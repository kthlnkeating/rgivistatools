package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.AdapterSupply;


public class TFNull extends TokenFactory {
	public TFNull(String name) {
		super(name);
	}
	 
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) {
		return null;
	}
}
