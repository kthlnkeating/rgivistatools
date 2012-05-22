package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TRule extends TDelimitedList implements TopTFRule {
	public TRule(List<Token> token) {
		super(token);
	}
	
	@Override
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		if (asShell) {
			return this.getTopFactoryShell(name, symbols);
		} else {
			return this.getTopFactory(name, symbols);
		}
	}
	
	@Override
	public TopTFRule preprocess(Map<String, TopTFRule> existingRules) {
		return this;
	}

	public TokenFactory getTopFactoryShell(String name, Map<String, TokenFactory> symbols) {
		return new TFSequence(name);
	}
		
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> symbols) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();
		int index = 0;
		for (Iterator<Token> it=this.iterator(); it.hasNext(); ++index) {
			FactorySupplyRule rpg = (FactorySupplyRule) it.next();
			TokenFactory f = rpg.getFactory(name + "." + String.valueOf(index), symbols);
			if (f == null) {
				return null;
			}
			boolean b = rpg.getRequired();
			factories.add(f);
			flags.add(b);
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

	TopTFRule getTopTFRule(String name, Map<String, TopTFRule> existingRules) {
		if (this.size() == 0) {
			throw new ParseErrorException("Rule " + name + " is empty.");
		}
		if (this.size() == 1) {
			try {
				TopTFRule result = (TopTFRule) this.get(0);
				return result.preprocess(existingRules);
			} catch (ClassCastException e) {
				throw new ParseErrorException("Rule " + name + " is invalid for a top level.");
				
			}
		}		
		return this;
	}
}
