package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.OrderedNameContainer;
import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public class FSRCopy extends FSRBase {
	private String name;
	private FactorySupplyRule slave;
	
	public FSRCopy(String name, FactorySupplyRule slave) {
		super(RuleSupplyFlag.TOP);
		this.name = name;
		this.slave = slave;
	}
	
	@Override
	public String getName() {
		return this.slave.getName();
	}
	
	@Override
	public FactorySupplyRule getLeading(OrderedNameContainer names) {
		return this.slave;
	}
	
	@Override
	public TokenFactory getFactory(RulesByName symbols) {
		return this.slave.getFactory(symbols);
	}

	@Override
	public TFBasic getShellFactory() {
		return (TFBasic) this.slave.getShellFactory();
	}
	
	@Override
	public int getSequenceCount() {
		return this.slave.getSequenceCount();
	}

	@Override
	public boolean update(RulesByName symbols) {
		return this.slave.update(symbols);
	}

}