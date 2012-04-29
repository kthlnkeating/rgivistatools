package com.raygroupintl.bnf;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;

public class TFSStatic implements ITokenFactorySupply {
	private ITokenFactory[] factories = {};
	
	public TFSStatic() {
	}
		
	public TFSStatic(ITokenFactory[] factories) {
		this.factories = factories;
	}
	
	public void setFactories(ITokenFactory[] factories) {
		this.factories = factories;
	}
	
	@Override
	public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
		if (seqIndex < factories.length) {				
			return factories[seqIndex];
		} else {
			return null;
		}
	}

	@Override
	public int getCount() {
		return factories.length;
	}
}
