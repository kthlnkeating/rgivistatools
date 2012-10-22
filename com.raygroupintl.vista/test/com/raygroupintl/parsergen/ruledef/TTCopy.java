package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Token;

public class TTCopy implements Token {
	private Token master;
	
	public TTCopy(Token token) {
		this.master = token;
	}
	
	@Override
	public TextPiece toValue() {
		return this.master.toValue();
	}
}
