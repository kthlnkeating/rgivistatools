package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.ActualList;
import com.raygroupintl.m.parsetree.IgnorableNode;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

public class MActualList extends MSequence {
	public MActualList(Token token) {
		super(token);
	}

	@Override
	public Node getNode() {		
		TList tokens = (TList) this.get(1);
		if (tokens == null) {
			return new IgnorableNode();
		} else {
			int size = tokens.size();
			ActualList nodes = new ActualList(size);
			for (Token t : tokens) {
				Node node = ((MToken) t).getNode();
				nodes.add(node);
			}
			return nodes;
		}
	}
}
