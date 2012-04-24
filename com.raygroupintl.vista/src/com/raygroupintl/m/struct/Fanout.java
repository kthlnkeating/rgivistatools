package com.raygroupintl.m.struct;

public class Fanout {
	enum FanoutType {
		DO,
		GOTO,
		FUNCTION,
		NOQUIT
	}
	
	private String routineName;
	private LineLocation location;
	private FanoutType type;
	
	public Fanout(String routineName, LineLocation location, FanoutType type) {
		this.routineName = routineName;
		this.location = location;
		this.type = type;
	}
	
	public String getRoutineName() {
		return this.routineName;
	}
	
	public LineLocation getLocation() {
		return this.location;
	}
	
	public FanoutType getType() {
		return this.type;
	}
}
