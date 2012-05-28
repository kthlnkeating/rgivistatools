package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TokenFactory;

public class FSRList extends FSRBase {
	private FactorySupplyRule element;
	private TFList factory;
	
	public FSRList(String name, RuleSupplyFlag flag, FactorySupplyRule element) {
		super(flag);
		this.element = element;
		this.factory = new TFList(name);
	}
	
	@Override
	public TFList getFactory(TokenFactoriesByName symbols) {
		TokenFactory element = this.element.getFactory(symbols);
		if (element == null) {
			return null;
		}
		this.factory.setElement(element);				
		return this.factory;		
	}

	@Override
	public TFList getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}