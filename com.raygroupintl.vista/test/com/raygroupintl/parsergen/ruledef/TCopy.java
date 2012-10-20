package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Token;

public class TCopy implements Token {
	private Token master;
	
	public TCopy(Token token) {
		this.master = token;
	}
	
	public void beautify() {
		this.master.beautify();
	}
	
	@Override
	public TextPiece toValue() {
		return this.master.toValue();
	}
}
