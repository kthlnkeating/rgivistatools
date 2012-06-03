package com.raygroupintl.parser;

import com.raygroupintl.parser.annotation.ObjectSupply;

public class TFEnd extends TokenFactory {
	public TFEnd(String name) {
		super(name);
	}
	
	@Override
	public Token tokenize(Text text, ObjectSupply objectSupply) {
		if (text.onChar()) {
			return null;
		} else {
			return objectSupply.newEmpty();
		}
	}
}
