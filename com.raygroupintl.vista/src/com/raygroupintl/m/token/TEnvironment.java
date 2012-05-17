package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.parsetree.IgnorableNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Token;

public class TEnvironment extends MTSequence {	
	public TEnvironment(List<Token> tokens) {
		super(tokens);
	}

	@Override
	public Node getNode() {
		return new IgnorableNode();
	}
}
