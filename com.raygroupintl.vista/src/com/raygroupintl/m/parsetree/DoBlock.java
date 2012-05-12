package com.raygroupintl.m.parsetree;

public class DoBlock extends Block<Line> {
	private int index;

	public DoBlock(int index) {
		this.index = index;
	}
	
	public String getKey() {
		return String.valueOf(this.index);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitDoBlock(this);
	}
}
