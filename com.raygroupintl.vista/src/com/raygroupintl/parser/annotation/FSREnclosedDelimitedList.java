package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

public class FSREnclosedDelimitedList extends FSRBase {
	private static class TokenFactoriesByNamesAndSelf implements TokenFactoriesByName {
		private TokenFactoriesByName factories;
		private TokenFactory self;
		
		public TokenFactoriesByNamesAndSelf(TokenFactoriesByName factories, TokenFactory self) {
			this.factories = factories;
			this.self = self;
		}
		
		@Override
		public TokenFactory get(String name) {
			if (self.getName().equals(name)) {
				return self;
			} else {
				return this.factories.get(name);
			}
		}
		
		@Override
		public void put(String name, TokenFactory f) {
			this.factories.put(name, f);
		}
		
		@Override
		public boolean isInitialized(TokenFactory f) {
			if (f == this.self) {
				return true;
			} else {
				return f.isInitialized();
			}
		}
	}

	private FactorySupplyRule element;
	private FactorySupplyRule delimiter;
	private FactorySupplyRule left;
	private FactorySupplyRule right;
	private boolean empty;
	private boolean none;
	private TFSequence factory;
	
	public FSREnclosedDelimitedList(String name, RuleSupplyFlag flag, FactorySupplyRule element, FactorySupplyRule delimiter, FactorySupplyRule left, FactorySupplyRule right) {
		super(flag);
		this.element = element;
		this.delimiter = delimiter;
		this.left = left;
		this.right = right;
		this.factory = new TFSequence(name);
	}
	
	public void setEmptyAllowed(boolean b) {
		this.empty = b;
	}
	
	
	public void setNoneAllowed(boolean b) {
		this.none = b;
	}
		
	@Override
	public TFSequence getFactory(TokenFactoriesByName symbols) {
		String name = this.factory.getName();
		TFSequence result = new TFSequence(name);
		TokenFactoriesByNamesAndSelf localSymbols = new TokenFactoriesByNamesAndSelf(symbols, result);
		TokenFactory e = this.element.getFactory(localSymbols);
		if (e == null) {
			return null;
		}
		TokenFactory d = this.delimiter.getFactory(symbols);
		TFDelimitedList dl = new TFDelimitedList(name);		
		dl.set(e, d, this.empty);
		TokenFactory l = this.left.getFactory(symbols);
		TokenFactory r = this.right.getFactory(symbols);
		TokenFactory[] factories = {l, dl, r};
		boolean[] required = {true, ! this.none, true};
		result.setFactories(factories, required);
		
		this.factory.copyWoutAdapterFrom(result);				
		return this.factory;		
	}

	@Override
	public TFSequence getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}