package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.Token;

public class TName extends TSequence {
	public TName(List<Token> tokens) {
		super(tokens);
	}
}
