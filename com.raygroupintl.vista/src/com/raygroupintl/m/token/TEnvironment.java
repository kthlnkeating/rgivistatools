package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.m.parsetree.IgnorableNode;
import com.raygroupintl.m.parsetree.Node;

public class TEnvironment extends MTArray {	
	public TEnvironment(Token[] tokens) {
		super(tokens);
	}

	@Override
	public Node getNode() {
		return new IgnorableNode();
	}
}
