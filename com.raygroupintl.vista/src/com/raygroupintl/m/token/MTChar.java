package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TChar;
import com.raygroupintl.m.parsetree.Node;

public class MTChar extends TChar implements MToken {
	public MTChar(char ch) {
		super(ch);
	}
	
	@Override
	public Node getNode() {
		return null;
	}
}
