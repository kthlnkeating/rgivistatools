package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

public class MLocal extends MSequence {
	public MLocal(Token token) {
		super(token);
	}

	@Override
	public Node getNode() {
		StringPiece name = this.get(0).toValue();
		Token subsriptsAll = this.get(1);
		if (subsriptsAll == null) {
			return new Local(name);
		} else {
			TList subscripts = (TList) subsriptsAll.toList().get(1);
			int size = subscripts.size();
			NodeList<Node> nodes = new NodeList<>(size);
			for (Token t : subscripts) {
				Node node = ((MToken) t).getNode();
				nodes.add(node);
			}
			return new Local(name, nodes);
		}
	}
}
