package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.StringPiece;
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
	public StringPiece toValue() {
		return this.master.toValue();
	}	
}
