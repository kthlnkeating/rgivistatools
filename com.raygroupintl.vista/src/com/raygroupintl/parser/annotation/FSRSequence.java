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
		public void put(String name, TokenFactory f, FactorySupplyRule r) {
			this.factories.put(name, f, r);
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
	private TFSequence factory;	
	
	public FSRSequence(String name, RuleSupplyFlag flag) {
		super(flag);
		this.factory = new TFSequence(name);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
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
	public TFSequence getFactory(TokenFactoriesByName symbols) {
		TokenFactoriesByNamesAndSelf localSymbols = new TokenFactoriesByNamesAndSelf(symbols, this.factory);
		
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();	
		for (FactorySupplyRule spg : this.list) {
			TokenFactory f = spg.getFactory(localSymbols);
			if (f == null) {
				return null;
			}
			boolean b = spg.getRequired();
			factories.add(f);
			flags.add(b);
		}

		int n = factories.size();
		TokenFactory[] fs = new TokenFactory[n];
		boolean[] bs = new boolean[n];
		for (int i=0; i<n; ++i) {
			fs[i] = factories.get(i);
			bs[i] = flags.get(i);
		}		
		this.factory.setFactories(fs, bs);
		return this.factory;		
	}

	@Override
	public TokenFactory getShellFactory(TokenFactoriesByName symbols) {
		return this.factory;
	}
}
