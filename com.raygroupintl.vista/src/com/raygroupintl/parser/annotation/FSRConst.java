package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TokenFactory;

public class FSRConst extends FSRBase {
	private String value;
	private TokenFactory factory;
	
	public FSRConst(String value, boolean ignoreCase, RuleSupplyFlag flag) {
		super(flag);
		this.value = value;
		String key = "\"" + this.value + "\"";
		this.factory = new TFConstant(key, this.value, ignoreCase);
	}
	
	@Override
	public String getName() {
		return "\"" + this.value + "\"";
	}
	
	@Override
	public TokenFactory getShellFactory() {
		return this.factory;
	}
}