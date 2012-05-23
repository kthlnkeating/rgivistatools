package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TokenFactory;

public class FSRConst extends FSRBase {
	private String value;
	
	public FSRConst(String value, boolean required) {
		super(required);
		this.value = value;
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		String key = "\"" + this.value + "\"";
		TokenFactory result = symbols.get(key);
		if (result == null) {		
			result = new TFConstant(key, this.value);
			symbols.put(key, result);
		}
		return result;
	}
}