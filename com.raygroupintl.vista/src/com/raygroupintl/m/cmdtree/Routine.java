package com.raygroupintl.m.cmdtree;

public class Routine extends Block<Entry> {
	private String name;
	
	public Routine(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return this.name;
	}
}
