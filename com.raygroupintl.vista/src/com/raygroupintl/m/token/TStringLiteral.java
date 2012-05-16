package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.Token;

public class TStringLiteral extends TSequence {
	public TStringLiteral(List<Token> tokens) {
		super(tokens);
	}
}
