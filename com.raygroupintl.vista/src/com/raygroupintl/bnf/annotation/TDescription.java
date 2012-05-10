package com.raygroupintl.bnf.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;

public class TDescription extends TSymbols {
	public TDescription(Token[] tokens) {
		super(convert(tokens));
	}
	
	private static TList convert(Token[] tokens) {
		if (tokens[1] == null) {
			return new TList(tokens[0]);
		} else {
			TList r = (TList) tokens[1];
			TList result = new TList(tokens[0]);
			for (int i=0; i<r.size(); ++i) {
				TArray re = (TArray) r.get(i);
				result.add(re.get(0));
				result.add(re.get(1));				
			}
			return result;
		}		
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

		TFSequenceStatic result = new TFSequenceStatic();
		
		int n = factories.size();
		TokenFactory[] fs = new TokenFactory[n];
		boolean[] bs = new boolean[n];
		for (int i=0; i<n; ++i) {
			fs[i] = factories.get(i);
			bs[i] = flags.get(i);
		}		
		result.setFactories(fs);
		result.setRequiredFlags(bs);
		return result;
	}

	@Override	
	public boolean getRequired() {
		return true;
	}	
}
