package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.OrderedName;
import com.raygroupintl.parser.OrderedNameContainer;
import com.raygroupintl.parser.TokenFactory;

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

	@Override
	public boolean update(RulesByName symbols) {
		return true;
	}

	public TokenFactory getTheFactory(RulesByName symbols) {
		FactorySupplyRule af = this.getActualRule(symbols);
		return af.getShellFactory();
	}

	@Override
	public OrderedName getLeading(OrderedNameContainer names) {
		return this;
	}
	
	@Override
	public FactorySupplyRule getActualRule(RulesByName symbols) {
		return this;
	}
	
	@Override
	public int getSequenceCount() {
		return 1;
	}
}
