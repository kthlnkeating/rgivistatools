package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

class FSRSequence extends FSRBase {
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
	public TFSequence getFactory(RulesByName symbols) {
		RulesByNameLocal localSymbols = new RulesByNameLocal(symbols, this);
		
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
	public TokenFactory getShellFactory() {
		return this.factory;
	}
}
