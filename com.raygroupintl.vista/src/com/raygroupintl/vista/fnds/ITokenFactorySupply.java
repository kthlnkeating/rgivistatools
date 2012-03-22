package com.raygroupintl.vista.fnds;

public interface ITokenFactorySupply {
	ITokenFactory get(IToken[] previousTokens);
	
	int getCount();
}
