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
	
	@Override
	public FactorySupplyRule formList(String name, RuleSupplyFlag flag, ListInfo info) {
		if (info.delimiter == null) {
			return new FSRList(name, flag, this);
		}
		if ((info.left == null) || (info.right == null)) {
			return new FSRDelimitedList(name, flag, this, info.delimiter);		
		}
		{
			FSREnclosedDelimitedList result = new FSREnclosedDelimitedList(name, flag, this, info.delimiter, info.left, info.right);
			result.setEmptyAllowed(info.emptyAllowed);
			result.setNoneAllowed(info.noneAllowed);
			return result;
		}		
	}
}
