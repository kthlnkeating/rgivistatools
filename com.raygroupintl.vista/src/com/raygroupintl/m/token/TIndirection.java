package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.fnds.IToken;

public class TIndirection extends TArray {
	public TIndirection(IToken[] tokens) {
		super(tokens);
	}	
}

/*if (tokens[1] == null) {
	TArray t = (TArray) tokens[0];
	return new TIndirection(t.get(1));			
} else {		
	TArray tReqArray = (TArray) tokens[0];
	ITokenArray tOptArray = (ITokenArray) tokens[1];
	IToken subscripts = tOptArray.get(1);
	return new TIndirection(tReqArray.get(1), subscripts);
}

*/