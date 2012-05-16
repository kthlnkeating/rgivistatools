package com.raygroupintl.bnf.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public abstract class TSymbols extends TArray implements SequencePieceGenerator {
	public TSymbols(List<Token> tokens) {
		super(tokens);
	}

	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();	
		TList list = (TList) this.get(1);
		int index = 0;
		for (Iterator<Token> it=list.iterator(); it.hasNext(); ++index) {
			Token t = it.next();
			if (t instanceof SequencePieceGenerator) {
				SequencePieceGenerator spg = (SequencePieceGenerator) t;
				TokenFactory f = spg.getFactory(name + "." + String.valueOf(index), map);
				boolean b = spg.getRequired();
				factories.add(f);
				flags.add(b);
			}
		}
		if (factories.size() == 0) return null;
		if (factories.size() == 1) return factories.get(0);
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
