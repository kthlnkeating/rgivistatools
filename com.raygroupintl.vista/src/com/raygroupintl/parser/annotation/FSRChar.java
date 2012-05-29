package com.raygroupintl.parser.annotation;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFCharacter;

public class FSRChar extends FSRBase {
	private String expr;
	private Predicate predicate;
	private TFCharacter factory;
	
	public FSRChar(String expr, RuleSupplyFlag flag, Predicate predicate) {
		super(flag);
		this.expr = expr;
		this.predicate = predicate;
		this.factory = new TFCharacter(this.expr, this.predicate);
	}
	
	@Override
	public String getName() {
		return this.expr;
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		if (! symbols.hasRule(expr)) {
			symbols.put(this.expr, this);
		}
		return true;
	}
	
	@Override
	public TFBasic getShellFactory() {
		return this.factory;
	}
	
	@Override
	public FactorySupplyRule formList(String name, RuleSupplyFlag flag, ListInfo info) {
		if ((info.delimiter != null) || (info.left != null) || (info.right != null)) {
			throw new UnsupportedOperationException("Delimiters and/or enclosers are not supported for list of character based symbols");
		}		
		return new FSRString("{" + this.expr + "}", flag, this.predicate);
	}
}
