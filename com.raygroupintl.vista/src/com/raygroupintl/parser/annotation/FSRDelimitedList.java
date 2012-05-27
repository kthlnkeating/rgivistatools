package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TokenFactory;

public class FSRDelimitedList extends FSRBase {
	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	private TFDelimitedList factory;
	
	public FSRDelimitedList(String name, FactorySupplyRule element, FactorySupplyRule delimiter, boolean required) {
		super(required);
		this.element = element;
		this.delimiter = delimiter;
		this.factory = new TFDelimitedList(name);
	}
	
	@Override
	public TFDelimitedList getFactory(TokenFactoriesByName symbols) {
		String name = this.factory.getName();
		TokenFactory element = this.element.getFactory(symbols);
		if (element == null) {
			return null;
		}
		TokenFactory delimiter = this.delimiter.getFactory(symbols);
		TFDelimitedList r = new TFDelimitedList(name);
		r.set(element, delimiter, false);
		
		this.factory.copyWoutAdapterFrom(r);				
		return this.factory;		
	}

	@Override
	public TFDelimitedList getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}