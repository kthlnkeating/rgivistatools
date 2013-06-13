package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.TokenType;
import com.raygroupintl.parsergen.rulebased.Rule;

public class GrammarInError0 {
	@Rule("{'a'...'z'}")
	public TokenFactory<Token> name;

	@TokenType(TTNameA.class)
	@Rule("namex")
	public TokenFactory<Token> namea;
}
