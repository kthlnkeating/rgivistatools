package com.raygroupintl.bnf.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public abstract class TSymbols extends TList implements SequencePieceGenerator {
	public TSymbols(TList list) {
		super(list);
	}

	protected static TList convertEnclosed(Token[] tokens) {
		TList result = new TList(tokens[0]);
		TList r = (TList) tokens[1];
		result.add(r.get(0));
		for (int i=1; i<r.size(); ++i) {
			TArray re = (TArray) r.get(i);
			result.add(re.get(0));
			result.add(re.get(1));				
		}
		result.add(tokens[2]);
		return result;
	}
	
	@Override
	public TokenFactory getFactory(Map<String, TokenFactory> map) {
		List<TokenFactory> factories = new ArrayList<TokenFactory>();
		List<Boolean> flags = new ArrayList<Boolean>();		
		for (int i=0; i<this.size(); ++i) {
			Token t = this.get(i);
			if (t instanceof SequencePieceGenerator) {
				SequencePieceGenerator spg = (SequencePieceGenerator) t;
				TokenFactory f = spg.getFactory(map);
				boolean b = spg.getRequired();
				factories.add(f);
				flags.add(b);
			}
		}
		if (factories.size() == 0) return null;
		if (factories.size() == 1) return factories.get(0);
		TFSequenceStatic result = new TFSequenceStatic();
		
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
