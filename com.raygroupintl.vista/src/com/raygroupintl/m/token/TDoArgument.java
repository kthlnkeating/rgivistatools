package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Token;

public class TDoArgument extends MTSequence {
	public TDoArgument(List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public Node getNode() {
		Node additionalNodes = super.getNode();
		AtomicDo result = new AtomicDo(additionalNodes);
		return result;
	}
}
