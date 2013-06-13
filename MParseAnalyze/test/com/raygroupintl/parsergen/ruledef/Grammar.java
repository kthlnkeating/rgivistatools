package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.SequenceTokenType;
import com.raygroupintl.parsergen.StringTokenType;
import com.raygroupintl.parsergen.TokenType;
import com.raygroupintl.parsergen.rulebased.Rule;

public class Grammar {
	@StringTokenType(TTIntLit.class)
	@Rule("'0'...'9'")
	public TokenFactory<Token> intlit;

	@Rule("'^'")
	public TokenFactory<Token> caret;

	@Rule("{'a'...'z'}")
	public TokenFactory<Token> name;

	@Rule("{name:',':'(':')'}")
	public TokenFactory<Token> params;
		
	@SequenceTokenType(TTLocal.class)
	@Rule("name, [params]")
	public TokenFactory<Token> local;

	@SequenceTokenType(TTObject.class)
	@Rule("name, {('.', name)}, [params]")
	public TokenFactory<Token> object;
	
	@SequenceTokenType(TTNumber.class)
	@Rule("['+' + '-'], ([intlit], ['.', intlit]), ['E', ['+' + '-'], intlit]")
	public TokenFactory<Token> number;
	
	@SequenceTokenType(TTGlobal.class)
	@Rule("caret, local")
	public TokenFactory<Token> global;
	
	@Rule("object | local | global | number")
	public TokenFactory<Token> expratom;

	@Rule("'+' | 'm'")
	public TokenFactory<Token> operator;

	@Rule("expratom, [{(operator, expratom)}]")
	public TokenFactory<Token> expr;
	
	@TokenType(TTNameA.class)
	@Rule("name")
	public TokenFactory<Token> namea;

	@TokenType(TTNameB.class)
	@Rule("name")
	public TokenFactory<Token> nameb;
	
	@Rule("namea, '^', name")
	public TokenFactory<Token> nameaseq;
	
	@Rule("nameb, ':', name")
	public TokenFactory<Token> namebseq;
	
	@Rule("name, intlit")
	public TokenFactory<Token> nameseq;
	
	@Rule("caret | number | nameaseq | namebseq")
	public TokenFactory<Token> testchoicea;
	
	@Rule("caret | number | name | nameaseq | namebseq")
	public TokenFactory<Token> testchoiceb;
	
	@Rule("nameseq | number | name | nameaseq")
	public TokenFactory<Token> testchoicec;
	
	@Rule("nameb | number | nameaseq | nameseq")
	public TokenFactory<Token> testchoiced;
}
