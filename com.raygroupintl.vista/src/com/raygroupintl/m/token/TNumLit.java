package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.Token;

public class TNumLit extends TSequence {
	public TNumLit(List<Token> tokens) {
		super(tokens);
	}
}
