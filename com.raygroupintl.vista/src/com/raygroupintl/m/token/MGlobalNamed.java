package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class MGlobalNamed extends MSequence {
	public MGlobalNamed(Token token) {
		super(token);
	}

	@Override
	public Node getNode() {
		TSequence actual = (TSequence) this.get(1);
		if (actual.get(0) != null) {
			return NodeUtilities.getNodes(actual, actual.size());
		} else {
			StringPiece name = actual.get(1).toValue();
			Token subsriptsAll = actual.get(2);
			if (subsriptsAll == null) {
				return new Global(name);
			} else {
				TList subscripts = (TList) subsriptsAll.toList().get(1);
				int size = subscripts.size();
				NodeList<Node> nodes = new NodeList<>(size);
				for (Token t : subscripts) {
					Node node = ((MToken) t).getNode();
					nodes.add(node);
				}
				return new Global(name, nodes);
			}
		}
	}
}
