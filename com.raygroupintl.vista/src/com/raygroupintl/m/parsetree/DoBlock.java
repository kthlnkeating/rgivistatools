package com.raygroupintl.m.parsetree;

public class DoBlock extends Block<Line> {
	public DoBlock() {
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitDoBlock(this);
	}
}
