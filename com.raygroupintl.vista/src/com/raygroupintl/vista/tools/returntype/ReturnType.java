package com.raygroupintl.vista.tools.returntype;

public class ReturnType {
	
	//basically just a prototype wrapper since ENUM's are singleton.
	private ReturnTypeENUM returnType;
	
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
