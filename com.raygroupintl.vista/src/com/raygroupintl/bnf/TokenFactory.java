package com.raygroupintl.bnf;

public interface TokenFactory {
	Token tokenize(String line, int fromIndex);
}