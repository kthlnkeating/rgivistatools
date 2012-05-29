package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.OrderedNameContainer;
import com.raygroupintl.parser.TFBasic;

public class FSRCopy extends FSRBase {
	private FactorySupplyRule slave;
	
	public FSRCopy(FactorySupplyRule slave) {
		super(RuleSupplyFlag.TOP);
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