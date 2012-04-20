package com.raygroupintl.vista.mtoken;

import java.util.Arrays;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.vista.fnds.IToken;

public class TLabelRef extends TArray {
	public TLabelRef(IToken[] tokens) {
		super(tokens);
	}
	
	public static TLabelRef getInstance(IToken[] tokens) {
		if (tokens.length > 2) throw new IllegalArgumentException();		
		IToken[] tokensCopy = Arrays.copyOf(tokens, 2);
		return new TLabelRef(tokensCopy);
	}	
}
