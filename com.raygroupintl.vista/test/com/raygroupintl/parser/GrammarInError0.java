package com.raygroupintl.parser;

import com.raygroupintl.parsergen.TokenType;
import com.raygroupintl.parsergen.rulebased.Rule;

public class GrammarInError0 {
	@Rule("{'a'...'z'}")
	public TokenFactory name;

	@TokenType(TNameA.class)
	@Rule("namex")
	public TokenFactory namea;
}
