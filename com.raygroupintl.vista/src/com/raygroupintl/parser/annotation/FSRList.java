package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TokenFactory;

public class FSRList extends FSRBase {
	private FactorySupplyRule element;
	
	public FSRList(FactorySupplyRule element, boolean required) {
		super(required);
		this.element = element;
	}
	
	@Override
	public TFList getFactory(String name, TokenFactoriesByName symbols) {
		TokenFactory element = this.element.getFactory(name + ".element", symbols);
		return new TFList(name, element);		
	}

	@Override
	public TFList getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		if (! asShell) {
			return this.getFactory(name, symbols);
		} else {			
			return new TFList(name);
		}
	}
}