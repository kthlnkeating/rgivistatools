package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFConstant;

public class FSRConst extends FSRBase {
	private String value;
	private TFBasic factory;
	
	public FSRConst(String value, RuleSupplyFlag flag) {
		super(flag);
		this.value = value;
		String key = "\"" + this.value + "\"";
		this.factory = new TFConstant(key, this.value);
	}
	
	@Override
	public String getName() {
		return "\"" + this.value + "\"";
	}
	
	@Override
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
	
	@Override
	public TFBasic getFactory(RulesByName symbols) {
		String key = "\"" + this.value + "\"";
		if (! symbols.hasRule(key)) {
			symbols.put(key, this);
		}
		return this.factory;
	}
	
	@Override
	public TFBasic getShellFactory() {
		return this.factory;
	}
}