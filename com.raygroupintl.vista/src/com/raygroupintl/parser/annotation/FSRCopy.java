package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public class FSRCopy extends FSRBase {
	private FactorySupplyRule slave;
	
	public FSRCopy(FactorySupplyRule slave) {
		super(RuleSupplyFlag.TOP);
		this.slave = slave;
	}
	
	@Override
	public TokenFactory getFactory(TokenFactoriesByName symbols) {
		return this.slave.getFactory(symbols);
	}

	@Override
	public TFBasic getShellFactory(TokenFactoriesByName symbols) {
		return (TFBasic) this.slave.getShellFactory(symbols);
	}
}