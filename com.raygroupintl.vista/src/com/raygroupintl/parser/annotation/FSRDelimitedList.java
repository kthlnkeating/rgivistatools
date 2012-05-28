package com.raygroupintl.parser.annotation;

import java.util.List;

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
	public TFDelimitedList getFactory(RulesByName symbols) {
		TokenFactory element = this.element.getFactory(symbols);
		if (element == null) {
			return null;
		}
		TokenFactory delimiter = this.delimiter.getFactory(symbols);
		
		this.factory.set(element, delimiter, false);				
		return this.factory;		
	}

	@Override
	public boolean update(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		this.element.update(localSymbols);
		this.delimiter.update(localSymbols);

		this.factory.set(this.element.getTheFactory(symbols), this.delimiter.getTheFactory(symbols), false);				
		return true;
	}

	@Override
	public TFDelimitedList getShellFactory() {
		return this.factory;
	}
}