package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

class FSRSequence extends FSRBase {
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>();
		
	public FSRSequence(boolean required) {
		super(required);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	
	@Override
	public boolean isSequence() {
		return true;
	}
	
	@Override
	public String getEntryKey() {
		return this.list.get(0).getEntryKey();
	}
		
	@Override
	public TFSequence getFactory(String name, Map<String, TokenFactory> symbols) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();	
		int index = 0;
		for (FactorySupplyRule spg : this.list) {
			TokenFactory f = spg.getFactory(name + "." + String.valueOf(index), symbols);
			if (f == null) {
				return null;
			}
			boolean b = spg.getRequired();
			factories.add(f);
			flags.add(b);
			++index;
		}

		TFSequence result = new TFSequence(name);
		
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
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (asShell) {
			return new TFSequence(name);
		} else {
			return this.getFactory(name, symbols);
		}
	}
}
