package com.raygroupintl.parser.annotation;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TokenFactory;

public class FSRString extends FSRBase {
	private String expr;
	private Predicate predicate;
	private TFBasic factory;
	
	public FSRString(String expr, RuleSupplyFlag flag, Predicate predicate) {
		super(flag);
		this.expr = expr;
		this.predicate = predicate;
		this.factory = new TFString(this.expr, this.predicate);
	}
	
	@Override
	public String getName() {
		return this.expr;
	}
	
	@Override
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
	
	@Override
	public TFBasic getFactory(TokenFactoriesByName symbols) {
		TokenFactory result = symbols.get(this.expr);
		if (result == null) {
			symbols.put(this.expr, this.factory);
		}
		return this.factory;
	}
	
	@Override
	public TFBasic getShellFactory(TokenFactoriesByName symbols) {
		return this.getFactory(symbols);
	}
}
	
