package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.TokenFactory;

public class RuleGrammar {
	@CharSpecified(chars={','}, single=true)
	public TokenFactory comma;
	
	@CharSpecified(chars={':'}, single=true)
	public TokenFactory colon;

	@CharSpecified(chars={'('}, single=true)
	public TokenFactory lpar;
	@CharSpecified(chars={')'}, single=true)
	public TokenFactory rpar;
	
	@CharSpecified(chars={'['}, single=true)
	public TokenFactory lsqr;
	@CharSpecified(chars={']'}, single=true)
	public TokenFactory rsqr;
	
	@CharSpecified(chars={'{'}, single=true)
	public TokenFactory lcur;
	@CharSpecified(chars={'}'}, single=true)
	public TokenFactory rcur;
	
	@CharSpecified(chars={'\''}, single=true)
	public TokenFactory squote;
	@CharSpecified(excludechars={'\''}, single=true)
	public TokenFactory squoted;

	@CharSpecified(chars={'"'}, single=true)
	public TokenFactory quote;
	@CharSpecified(excludechars={'"'})
	public TokenFactory quoted;

	@CharSpecified(chars={'|'}, single=true)
	public TokenFactory pipe;
	
	@CharSpecified(chars={'+', '-'}, single=true)
	public TokenFactory pm;
	
	@WordSpecified("...")
	public TokenFactory ellipsis;

	@TokenType(TSymbol.class)
	@CharSpecified(ranges={'a', 'z'})
	public TokenFactory specifiedsymbol; 

	@TokenType(TCharSymbol.class)
	@Sequence(value={"squote", "squoted", "squote"}, required="all")
	public TokenFactory charsymbol; 

	@Sequence(value={"ellipsis", "charsymbol"}, required="all")
	public TokenFactory charsymbolto; 
	@Sequence(value={"charsymbol", "charsymbolto", "sp"}, required="roo")
	public TokenFactory charsymbolwr; 
	@Sequence(value={"dpm", "charsymbolwr"}, required="all")
	public TokenFactory charsymbolpp; 
	@List(value="charsymbolpp")
	public TokenFactory charsymbollist; 
	@TokenType(TCharSymbol.class)
	@Sequence(value={"dpm", "charsymbolwr", "charsymbollist"}, required="oro")
	public TokenFactory charsymbolall; 

	@TokenType(TConstSymbol.class)
	@Sequence(value={"quote", "quoted", "quote"}, required="all")
	public TokenFactory constsymbol; 
	
	@Choice({"specifiedsymbol", "charsymbolall", "constsymbol", "optionalsymbols", "requiredsymbols", "list"})
	public TokenFactory symbol; 
	
	@TokenType(TChoice.class)
	@List(value="symbol", delim="choicedelimiter")
	public TokenFactory symbolchoice; 

	@CharSpecified(chars={' '})
	public TokenFactory sp;

	@Sequence(value={"sp", "lpar", "sp"}, required="oro")
	public TokenFactory openrequired;
	@Sequence(value={"sp", "rpar", "sp"}, required="oro")
	public TokenFactory closerequired;

	@Sequence(value={"pm", "sp"}, required="ro")
	public TokenFactory dpm;	
	
	@Sequence(value={"sp", "lsqr", "sp"}, required="oro")
	public TokenFactory openoptional;
	@Sequence(value={"sp", "rsqr", "sp"}, required="oro")
	public TokenFactory closeoptional;
	
	@Sequence(value={"sp", "lcur", "sp"}, required="oro")
	public TokenFactory openlist;
	@Sequence(value={"sp", "rcur", "sp"}, required="oro")
	public TokenFactory closelist;
	
	@Sequence(value={"sp", "pipe", "sp"}, required="oro")
	public TokenFactory choicedelimiter;

	@Sequence(value={"sp", "comma", "sp"}, required="oro")
	public TokenFactory delimiter;
	
	@TokenType(TOptionalSymbols.class)
	@List(value="symbolchoice", delim="delimiter", left="openoptional", right="closeoptional")
	public TokenFactory optionalsymbols; 
	@TokenType(TRequiredSymbols.class)
	@List(value="symbolchoice", delim="delimiter", left="openrequired", right="closerequired")
	public TokenFactory requiredsymbols;
	
	@Sequence(value={"colon", "anysymbols", "colon", "anysymbols"}, required="all")
	public TokenFactory leftrightspec;
	@Sequence(value={"colon", "anysymbols", "leftrightspec"}, required="roo")
	public TokenFactory delimleftrightspec;
	@TokenType(TSymbolList.class)
	@Sequence(value={"openlist", "symbolchoice", "delimleftrightspec", "closelist"}, required="rror")
	public TokenFactory list;
	
	@Choice({"specifiedsymbol", "charsymbolall", "constsymbol"})
	public TokenFactory anysymbols;
	
	@TokenType(TRule.class)
	@List(value="symbolchoice", delim="delimiter")
	public TokenFactory rule;
}
