package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Token;

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
	public boolean getRequired() {
		return true;
	}	
}
