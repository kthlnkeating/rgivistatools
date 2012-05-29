package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFConstant;

public class FSRConst extends FSRBase {
	private String value;
	private TFBasic factory;
	
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
	public TFBasic getShellFactory() {
		return this.factory;
	}
}