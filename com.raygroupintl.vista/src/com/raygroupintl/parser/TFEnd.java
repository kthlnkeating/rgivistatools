package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFEnd extends TokenFactory {
	public TFEnd(String name) {
		super(name);
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) {
		if (text.onChar()) {
			return null;
		} else {
			return new TEmpty();
		}
	}
}
