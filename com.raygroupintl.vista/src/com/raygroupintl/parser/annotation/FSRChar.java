package com.raygroupintl.parser.annotation;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TokenFactory;

public class FSRChar extends FSRBase {
	private String expr;
	private Predicate predicate;
	private boolean inList;
	
	public FSRChar(String expr, Predicate predicate, boolean required) {
		super(required);
		this.expr = inList ? "{" + expr + "}" : expr;
		this.predicate = predicate;
	}
	
	void setInList(boolean b) {
		this.inList = b;
		if (b) {
			this.expr = "{" + this.expr + "}";
		} else {
			this.expr = this.expr.substring(1, this.expr.length()-1);
		}
	}
	
	@Override
	public TFBasic getFactory(TokenFactoriesByName symbols) {
		TokenFactory result = symbols.get(this.expr);
		if (result == null) {
			if (this.inList) {
				result = new TFString(this.expr, this.predicate);			
			} else {
				result = new TFCharacter(this.expr, this.predicate);
			} 
			symbols.put(this.expr, result);
		}
		return (TFBasic) result;
	}
	
	@Override
	public TFBasic getShellFactory(TokenFactoriesByName symbols) {
		return this.getFactory(symbols);
	}
}
