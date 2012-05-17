package com.raygroupintl.m.struct;


public class MLocationedError {
	private MError error;
	private LineLocation location;
	
	public MLocationedError(MError error, LineLocation location) {
		this.error = error;
		this.location = location;
	}
	
	public MError getError() {
		return this.error;
	}
	
	public LineLocation getLocation() {
		return this.location;
	}
}
