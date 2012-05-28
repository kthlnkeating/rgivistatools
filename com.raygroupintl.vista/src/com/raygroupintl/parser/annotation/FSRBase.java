package com.raygroupintl.parser.annotation;

public abstract class FSRBase implements FactorySupplyRule {
	private RuleSupplyFlag flag;
	
	public FSRBase(RuleSupplyFlag flag) {
		this.flag = flag;
	}
	
	@Override
	public boolean getRequired() {
		switch (this.flag) {
			case INNER_OPTIONAL: 
				return false;
			case INNER_REQUIRED: 
				return true;
			default:
				throw new ParseErrorException("Intenal error: attempt to get required flag for a top symbol.");
		}
	}
}
