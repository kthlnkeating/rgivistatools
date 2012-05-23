package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TokenFactory;

class FSRSequence extends FSRBase implements TopTFRule {
	private List<FactorySupplyRule> list = new ArrayList<FactorySupplyRule>();
		
	public FSRSequence(boolean required) {
		super(required);
	}
	
	public void add(FactorySupplyRule r) {
		this.list.add(r);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
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
		if (factories.size() == 0) return null;
		if (factories.size() == 1) return factories.get(0);
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
			return this.getTopFactoryShell(name, symbols);
		} else {
			return this.getFactory(name, symbols);
		}
	}
	
	public TokenFactory getTopFactoryShell(String name, Map<String, TokenFactory> symbols) {
		return new TFSequence(name);
	}
}
