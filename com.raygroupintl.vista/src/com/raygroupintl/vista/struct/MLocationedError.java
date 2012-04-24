package com.raygroupintl.vista.struct;

import com.raygroupintl.m.struct.LineLocation;

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
