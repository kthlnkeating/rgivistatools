package com.raygroupintl.m.parsetree;

public class ForBlock extends Block<Node> {
	public ForBlock() {		
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitForBlock(this);
	}
}
