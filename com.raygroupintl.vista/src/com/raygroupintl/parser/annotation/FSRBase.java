package com.raygroupintl.parser.annotation;

public abstract class FSRBase implements FactorySupplyRule {
	private boolean required;
	
	public FSRBase(boolean required) {
		this.required = required;
	}
	
	@Override
	public boolean getRequired() {
		return this.required;
	}
}
