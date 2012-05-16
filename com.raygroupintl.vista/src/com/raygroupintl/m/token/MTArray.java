package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.Token;

public abstract class MTArray extends TSequence implements MToken {
	public MTArray(List<Token> tokens) {
		super(tokens);
	}
}
