package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TChar;

public class MTChar extends TChar implements MToken {
	public MTChar(char ch) {
		super(ch);
	}
	
	@Override
	public Node getNode() {
		return null;
	}
}
