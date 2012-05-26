package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TokenFactory;

public class FSRConst extends FSRBase {
	private String value;
	
	public FSRConst(String value, boolean required) {
		super(required);
		this.value = value;
	}
	
	@Override
	public TFBasic getFactory(String name, TokenFactoriesByName symbols) {
		String key = "\"" + this.value + "\"";
		TokenFactory result = symbols.get(key);
		if (result == null) {		
			result = new TFConstant(key, this.value);
			symbols.put(key, result);
		}
		return (TFBasic) result;
	}
	
	@Override
	public TFBasic getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		return this.getFactory(name, symbols);
	}
}