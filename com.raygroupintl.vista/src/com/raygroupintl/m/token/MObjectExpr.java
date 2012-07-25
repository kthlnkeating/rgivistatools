package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.ObjectMethodCall;
import com.raygroupintl.parser.Token;

public class MObjectExpr extends MSequence {
	public MObjectExpr(Token token) {
		super(token);
	}
	
	@Override
	public Node getNode() {
		Node additionalNodes = super.getNode();
		ObjectMethodCall result = new ObjectMethodCall(additionalNodes);
		return result;
	}
}
