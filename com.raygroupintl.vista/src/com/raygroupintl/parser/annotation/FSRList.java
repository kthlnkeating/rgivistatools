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
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		this.element.update(localSymbols);
		TokenFactory element = this.element.getTheFactory(localSymbols);
		this.factory.setElement(element);
		return true;
	}

	@Override
	public TFList getShellFactory() {
		return this.factory;
	}
}