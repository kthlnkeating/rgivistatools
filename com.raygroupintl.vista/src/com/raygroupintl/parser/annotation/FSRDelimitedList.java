package com.raygroupintl.parser.annotation;

import java.util.Map;

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
	public TFDelimitedList getFactory(String name, Map<String, TokenFactory> symbols) {
		TokenFactory element = this.element.getFactory(name + ".element", symbols);
		TokenFactory delimiter = this.delimiter.getFactory(name + ".delimiter", symbols);
		TFDelimitedList r = new TFDelimitedList(name);
		r.set(element, delimiter, false);
		return r;		
	}

	@Override
	public TFDelimitedList getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (! asShell) {
			return this.getFactory(name, symbols);
		} else {			
			return new TFDelimitedList(name);
		}
	}
}