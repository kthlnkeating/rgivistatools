package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.ExternalDo;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Token;

public class MExtDoArgument extends MSequence {
	public MExtDoArgument(Token token) {
		super(token);
	}
	
	@Override
	public Node getNode() {
		Node additionalNodes = super.getNode();
		ExternalDo result = new ExternalDo(additionalNodes);
		return result;
	}
}
