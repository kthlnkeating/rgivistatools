package com.raygroupintl.parser.annotation;

import java.util.Map;

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
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		return this.slave.getFactory(this.value, symbols);
	}

	@Override
	public TFBasic getTopFactory(String name, Map<String, TokenFactory> symbols, boolean asShell) {
		return (TFBasic) this.slave.getTopFactory(this.value, symbols, asShell);
	}
		
	@Override
	public String getEntryKey() {
		return this.value;
	}
}