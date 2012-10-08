package com.raygroupintl.parser;

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.Rule;
import com.raygroupintl.parser.annotation.SequenceTokenType;
import com.raygroupintl.parser.annotation.TokenType;

public class Grammar {
	@TokenType(TIntLit.class)
	@Rule("'0'...'9'")
	public TokenFactory intlit;

	@Rule("'^'")
	public TokenFactory caret;

	@Rule("{'a'...'z'}")
	public TokenFactory name;

	@Rule("{name:',':'(':')'}")
	public TokenFactory params;
		
	@SequenceTokenType(TLocal.class)
	@Rule("name, [params]")
	public TokenFactory local;

	@SequenceTokenType(TObject.class)
	@Rule("name, {('.', name)}, [params]")
	public TokenFactory object;
	
	@SequenceTokenType(TNumber.class)
	@Rule("['+' + '-'], ([intlit], ['.', intlit]), ['E', ['+' + '-'], intlit]")
	public TokenFactory number;
	
	@SequenceTokenType(TGlobal.class)
	@Rule("caret, local")
	public TokenFactory global;
	
	@Rule("object | local | global | number")
	public TokenFactory expratom;

	@Rule("'+' | 'm'")
	public TokenFactory operator;

	@Rule("expratom, [{(operator, expratom)}]")
	public TokenFactory expr;
	
	@TokenType(TNameA.class)
	@Rule("name")
	public TokenFactory namea;

	@TokenType(TNameB.class)
	@Rule("name")
	public TokenFactory nameb;
	
	@Rule("namea, '^', name")
	public TokenFactory nameaseq;
	
	@Rule("nameb, ':', name")
	public TokenFactory namebseq;
	
	@Rule("name, intlit")
	public TokenFactory nameseq;
	
	@Rule("caret | number | nameaseq | namebseq")
	public TokenFactory testchoicea;
	
	@Rule("caret | number | name | nameaseq | namebseq")
	public TokenFactory testchoiceb;
	
	@Rule("nameseq | number | name | nameaseq")
	public TokenFactory testchoicec;
	
	@Rule("nameb | number | nameaseq | nameseq")
	public TokenFactory testchoiced;
}
