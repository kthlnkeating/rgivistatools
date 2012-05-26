package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TokenFactory;

public class FSRDelimitedList extends FSRBase {
	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	
	public FSRDelimitedList(FactorySupplyRule element, FactorySupplyRule delimiter, boolean required) {
		super(required);
		this.element = element;
		this.delimiter = delimiter;
	}
	
	@Override
	public TFDelimitedList getFactory(String name, TokenFactoriesByName symbols) {
		TokenFactory element = this.element.getFactory(name + ".element", symbols);
		if (element == null) {
			return null;
		}
		TokenFactory delimiter = this.delimiter.getFactory(name + ".delimiter", symbols);
		TFDelimitedList r = new TFDelimitedList(name);
		r.set(element, delimiter, false);
		return r;		
	}

	@Override
	public TFDelimitedList getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		if (! asShell) {
			return this.getFactory(name, symbols);
		} else {			
			return new TFDelimitedList(name);
		}
	}
}