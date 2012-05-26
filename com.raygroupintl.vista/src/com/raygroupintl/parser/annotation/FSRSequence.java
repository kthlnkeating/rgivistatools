package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

class FSRSequence extends FSRBase {
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
		
	public FSRSequence(boolean required) {
		super(required);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	
	@Override
	public TFSequence getFactory(String name, TokenFactoriesByName symbols) {
		TFSequence result = new TFSequence(name);
		
		TokenFactoriesByNamesAndSelf localSymbols = new TokenFactoriesByNamesAndSelf(symbols, result);
		
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();	
		int index = 0;
		for (FactorySupplyRule spg : this.list) {
			TokenFactory f = spg.getFactory(name + "." + String.valueOf(index), localSymbols);
			if (f == null) {
				return null;
			}
			boolean b = spg.getRequired();
			factories.add(f);
			flags.add(b);
			++index;
		}

		int n = factories.size();
		TokenFactory[] fs = new TokenFactory[n];
		boolean[] bs = new boolean[n];
		for (int i=0; i<n; ++i) {
			fs[i] = factories.get(i);
			bs[i] = flags.get(i);
		}		
		result.setFactories(fs, bs);
		return result;
	}

	@Override
	public TokenFactory getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		if (asShell) {
			return new TFSequence(name);
		} else {
			return this.getFactory(name, symbols);
		}
	}
}
