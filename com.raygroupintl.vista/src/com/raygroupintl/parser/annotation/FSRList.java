package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TokenFactory;

public class FSRList extends FSRBase {
	private FactorySupplyRule element;
	private TFList factory;
	
	public FSRList(String name, FactorySupplyRule element, boolean required) {
		super(required);
		this.element = element;
		this.factory = new TFList(name);
	}
	
	@Override
	public TFList getFactory(TokenFactoriesByName symbols) {
		String name = this.factory.getName();
		TokenFactory element = this.element.getFactory(symbols);
		if (element == null) {
			return null;
		}
		TFList result = new TFList(name, element);		
		this.factory.copyWoutAdapterFrom(result);				
		return this.factory;		
	}

	@Override
	public TFList getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}