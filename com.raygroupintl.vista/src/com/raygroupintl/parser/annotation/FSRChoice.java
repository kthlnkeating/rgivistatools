package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TFForkableChoice;
import com.raygroupintl.parser.TokenFactory;

public class FSRChoice extends FSRBase {
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
	
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>(); 
	private TFForkableChoice factory;
	
	public FSRChoice(String name, boolean required) {
		super(required);
		this.factory = new TFForkableChoice(name);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	
	@Override
	public TFForkableChoice getFactory(String name, TokenFactoriesByName symbols) {
		assert name.equals(this.factory.getName());
		TFForkableChoice result = new TFForkableChoice(name);

		TokenFactoriesByNamesAndSelf localSymbols = new TokenFactoriesByNamesAndSelf(symbols, result);
			
		int index = 0;
		for (FactorySupplyRule r : this.list) {
			TokenFactory f = r.getFactory(name + String.valueOf(index), localSymbols);
			if (f == null) {
				return null;
			}
			result.add(f, symbols);
			++index;
		}
		
		this.factory.copyWoutAdapterFrom(result);		
		return this.factory;
	}

	@Override
	public TFBasic getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		if (asShell) {
			return this.factory;	
		} else {
			return this.getFactory(name, symbols);
		}
	}
}
