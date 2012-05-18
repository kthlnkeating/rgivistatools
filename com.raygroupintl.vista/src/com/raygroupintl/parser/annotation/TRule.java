package com.raygroupintl.parser.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFSequenceStatic;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TRule extends TDelimitedList implements RuleGenerator {
	public TRule(List<Token> token) {
		super(token);
	}
	
	@Override
	public TokenFactory getTopFactory(String name, Map<String, TokenFactory> map) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();
		int index = 0;
		for (Iterator<Token> it=this.iterator(); it.hasNext(); ++index) {
			Token t = it.next();
			if (t instanceof RulePieceGenerator) {
				RulePieceGenerator spg = (RulePieceGenerator) t;
				TokenFactory f = spg.getFactory(name + "." + String.valueOf(index), map);
				boolean b = spg.getRequired();
				factories.add(f);
				flags.add(b);
			}
		}
		if (factories.size() == 0) return null;

		TFSequenceStatic result = new TFSequenceStatic(name);
		
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
}
