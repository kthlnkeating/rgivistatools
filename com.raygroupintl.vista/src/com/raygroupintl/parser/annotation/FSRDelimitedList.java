package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TokenFactory;

public class FSRDelimitedList extends FSRBase {
	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	private TFDelimitedList factory;
	
	public FSRDelimitedList(String name, RuleSupplyFlag flag, FactorySupplyRule element, FactorySupplyRule delimiter) {
		super(flag);
		this.element = element;
		this.delimiter = delimiter;
		this.factory = new TFDelimitedList(name);
	}
	
	@Override
	public String getName() {
		return this.factory.getName();
	}
	
	@Override
	public FactorySupplyRule getLeadingRule() {
		return this;
	}
	
	@Override
	public TFDelimitedList getFactory(TokenFactoriesByName symbols) {
		TokenFactory element = this.element.getFactory(symbols);
		if (element == null) {
			return null;
		}
		TokenFactory delimiter = this.delimiter.getFactory(symbols);
		
		this.factory.set(element, delimiter, false);				
		return this.factory;		
	}

	@Override
	public TFDelimitedList getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}