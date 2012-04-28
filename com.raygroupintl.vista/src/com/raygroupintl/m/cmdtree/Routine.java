package com.raygroupintl.m.cmdtree;

public class Routine extends Block<EntryTag> {
	private String name;
	
	public Routine(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return this.name;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitRoutine(this);
	}
}
