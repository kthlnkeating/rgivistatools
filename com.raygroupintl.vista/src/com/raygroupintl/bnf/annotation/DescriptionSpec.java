package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.TokenFactory;

public class DescriptionSpec {
	@CharSpecified(chars={','}, single=true)
	public TokenFactory comma;
	
	@CharSpecified(chars={'('}, single=true)
	public TokenFactory lpar;
	@CharSpecified(chars={')'}, single=true)
	public TokenFactory rpar;
	
	@CharSpecified(chars={'['}, single=true)
	public TokenFactory lsqr;
	@CharSpecified(chars={']'}, single=true)
	public TokenFactory rsqr;
	
	@TokenType(TSymbol.class)
	@CharSpecified(ranges={'a', 'z'})
	public TokenFactory symbol; 

	@CharSpecified(chars={' '})
	public TokenFactory sp;

	@Sequence(value={"sp", "lpar", "sp"}, required="oro")
	public TokenFactory openrequired;
	@Sequence(value={"sp", "rpar", "sp"}, required="oro")
	public TokenFactory closerequired;
	
	@Sequence(value={"sp", "lsqr", "sp"}, required="oro")
	public TokenFactory openoptional;
	@Sequence(value={"sp", "rsqr", "sp"}, required="oro")
	public TokenFactory closeoptional;
	
	@Sequence(value={"sp", "comma", "sp"}, required="oro")
	public TokenFactory delimiter;
	
	@TokenType(TOptionalSymbols.class)
	@List(value="anysymbols", delim="delimiter", left="openoptional", right="closeoptional")
	public TokenFactory optionalsymbols; 
	@TokenType(TRequiredSymbols.class)
	@List(value="anysymbols", delim="delimiter", left="openrequired", right="closerequired")
	public TokenFactory requiredsymbols; 
	
	@Choice({"symbol", "optionalsymbols", "requiredsymbols"})
	public TokenFactory anysymbols;
	
	@TokenType(TDescription.class)
	@List(value="anysymbols", delim="delimiter")
	public TokenFactory description;
}
