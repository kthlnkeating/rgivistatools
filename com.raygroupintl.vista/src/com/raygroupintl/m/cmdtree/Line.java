package com.raygroupintl.m.cmdtree;

public class Line extends Block<Node> {
	private int index;
	
	public Line(int index) {
		this.index = index;
	}
	
	public Line(int index, int level) {
		this.index = index;
	}

	public String getKey() {
		return String.valueOf(this.index);
	}
}
