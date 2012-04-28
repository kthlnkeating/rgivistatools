package com.raygroupintl.m.cmdtree;

public class Fanout {
	private boolean environmentSpecified;
	private String routineName;
	private String tag;
	private int offset;
	
	public Fanout(String routineName, String tag) {
		this.routineName = routineName;
		this.tag = tag;
	}
	
	public String getRoutineName() {
		return routineName;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public boolean hasEnvironment() {
		return this.environmentSpecified;
	}
	
	public boolean hasOffset() {
		return this.offset > 0;
	}
}
