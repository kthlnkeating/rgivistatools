package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.Token;

public abstract class MTArray extends TArray implements MToken {
	public MTArray(List<Token> tokens) {
		super(tokens);
	}
}
