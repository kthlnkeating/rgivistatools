package com.raygroupintl.bnf;


public class TFSStatic implements TokenFactorySupply {
	private TokenFactory[] factories = {};
	
	public TFSStatic() {
	}
		
	public TFSStatic(TokenFactory[] factories) {
		this.factories = factories;
	}
	
	public void setFactories(TokenFactory[] factories) {
		this.factories = factories;
	}
	
	@Override
	public TokenFactory get(int seqIndex, Token[] previousTokens) {
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
