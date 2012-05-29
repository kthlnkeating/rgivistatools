package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TokenFactory;

public class FSRCustom extends FSRBase {
	private TokenFactory factory;
	
	public FSRCustom(TokenFactory factory) {
		super(RuleSupplyFlag.TOP);
		this.factory = factory;
	}
		
	@Override
	public TokenFactory getShellFactory() {
		return this.factory;
	}

	@Override
	public String getName() {
		return this.factory.getName();
	}
}
