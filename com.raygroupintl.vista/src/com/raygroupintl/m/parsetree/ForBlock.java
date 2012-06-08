package com.raygroupintl.m.parsetree;

public class ForBlock extends NodeList {
	public ForBlock() {		
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitForBlock(this);
	}
}
