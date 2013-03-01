package com.raygroupintl.vista.tools.returntype;

public class ReturnType {
	
	private ReturnTypeENUM returnType;
	//TODO: either put more values in here for tracking line locations and everything or create a new visitor for tracking line locations.

	public ReturnType(ReturnTypeENUM returnType) {
		super();
		this.returnType = returnType;
	}
	
	public ReturnType() {
		super();
	}

	public ReturnTypeENUM getReturnType() {
		return returnType;
	}

	public void setReturnType(ReturnTypeENUM returnType) {
		this.returnType = returnType;
	}

}
