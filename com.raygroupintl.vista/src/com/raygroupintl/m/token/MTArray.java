package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.Token;

public abstract class MTArray extends TArray implements MToken {
	public MTArray(Token[] tokens) {
		super(tokens);
	}
}
