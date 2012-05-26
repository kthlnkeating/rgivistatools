package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public class FSRSingle extends FSRBase {
	private String value;
	
	public FSRSingle(String value, boolean required) {
		super(required);
		this.value = value;
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		TokenFactory result = symbols.get(this.value);
		if (result == null) throw new ParseErrorException("Undefined symbol " + value + " used in the rule");
		if (! result.isInitialized()) {
			return null;
		}
		return result;
	}

	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		throw new ParseErrorException("Not a top rule.");
	}
}