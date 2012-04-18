package com.raygroupintl.vista.struct;

public class MLocationedError {
	private MError error;
	private MLineLocation location;
	
	public MLocationedError(MError error, MLineLocation location) {
		this.error = error;
		this.location = location;
	}
	
	public MError getError() {
		return this.error;
	}
	
	public MLineLocation getLocation() {
		return this.location;
	}
}
