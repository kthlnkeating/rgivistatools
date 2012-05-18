package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.parsetree.ExternalDo;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Token;

public class TExtDoArgument extends MTSequence {
	public TExtDoArgument(List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public Node getNode() {
		Node additionalNodes = super.getNode();
		ExternalDo result = new ExternalDo(additionalNodes);
		return result;
	}
}
