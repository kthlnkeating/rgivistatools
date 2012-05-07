package com.raygroupintl.bnf;


public interface TokenFactorySupply {
	TokenFactory get(int seqIndex, Token[] previousTokens);
	
	int getCount();
}
