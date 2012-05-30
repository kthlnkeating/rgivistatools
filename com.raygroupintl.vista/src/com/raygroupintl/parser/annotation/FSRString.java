package com.raygroupintl.parser.annotation;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFString;

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
	public boolean update(RulesByName symbols) {
		if (! symbols.hasRule(expr)) {
			symbols.put(this.expr, this);
		}
		return true;
	}
	
	@Override
	public String getName() {
		return this.expr;
	}
	
	@Override
	public TFBasic getShellFactory() {
		return this.factory;
	}
}
	
