package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TokenFactory;

public class DescriptionSpec {
	public TokenFactory comma = new TFConstChar(',');
	
	public TokenFactory lpar = new TFConstChar('(');
	public TokenFactory rpar = new TFConstChar(')');
	
	public TokenFactory lsqr = new TFConstChar('[');
	public TokenFactory rsqr = new TFConstChar(']');
	
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
