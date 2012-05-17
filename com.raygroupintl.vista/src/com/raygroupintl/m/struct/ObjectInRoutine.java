package com.raygroupintl.m.struct;


public class ObjectInRoutine<T> {
	private T object;
	private LineLocation location;
	
	public ObjectInRoutine(T object, LineLocation location) {
		this.object = object;
		this.location = location;
	}
	
	public T getObject() {
		return this.object;
	}
	
	public LineLocation getLocation() {
		return this.location;
	}
}
