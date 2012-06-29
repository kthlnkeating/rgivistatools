package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.StringLiteral;
import com.raygroupintl.parser.Token;

public class MStringLiteral extends MSequence {
	public MStringLiteral(Token token) {
		super(token);
	}

	@Override
	public Node getNode() {
		String value = this.toValue().toString();
		return new StringLiteral(value);
	}	
}
