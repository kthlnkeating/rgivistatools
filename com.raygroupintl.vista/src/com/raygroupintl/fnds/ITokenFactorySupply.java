package com.raygroupintl.fnds;

public interface ITokenFactorySupply {
	ITokenFactory get(int seqIndex, IToken[] previousTokens);
	
	int getCount();
}
