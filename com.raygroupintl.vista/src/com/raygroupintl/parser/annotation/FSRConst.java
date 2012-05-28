package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TokenFactory;

public class FSRConst extends FSRBase {
	private String value;
	
	public FSRConst(String value, RuleSupplyFlag flag) {
		super(flag);
		this.value = value;
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
	public TFBasic getFactory(TokenFactoriesByName symbols) {
		String key = "\"" + this.value + "\"";
		TokenFactory result = symbols.get(key);
		if (result == null) {		
			result = new TFConstant(key, this.value);
			symbols.put(key, result);
		}
		return (TFBasic) result;
	}
	
	@Override
	public TFBasic getShellFactory(TokenFactoriesByName symbols) {
		return this.getFactory(symbols);
	}
}