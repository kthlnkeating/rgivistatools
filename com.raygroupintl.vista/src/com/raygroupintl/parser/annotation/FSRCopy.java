package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TFBasic;
import com.raygroupintl.parser.TokenFactory;

public class FSRCopy extends FSRBase {
	private String value;
	private FactorySupplyRule slave;
	
	public FSRCopy(String value, FactorySupplyRule slave) {
		super(true);
		this.value = value;
		this.slave = slave;
	}
	
	@Override
	public TokenFactory getFactory(String name, TokenFactoriesByName symbols) {
		return this.slave.getFactory(this.value, symbols);
	}

	@Override
	public TFBasic getTopFactory(String name, TokenFactoriesByName symbols, boolean asShell) {
		return (TFBasic) this.slave.getTopFactory(this.value, symbols, asShell);
	}
}