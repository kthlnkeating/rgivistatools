package com.raygroupintl.bnf;

import java.util.List;


public interface TokenFactorySupply {
	TokenFactory get(int seqIndex, List<Token> previousTokens);
	
	int getCount();
}
