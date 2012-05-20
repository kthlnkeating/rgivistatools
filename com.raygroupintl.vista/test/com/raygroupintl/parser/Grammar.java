package com.raygroupintl.parser;

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.CharSpecified;
import com.raygroupintl.parser.annotation.Choice;
import com.raygroupintl.parser.annotation.Rule;
import com.raygroupintl.parser.annotation.TokenType;

public class Grammar {
	@TokenType(TIntLit.class)
	@CharSpecified(ranges={'0', '9'})
	public TokenFactory intlit;

	@CharSpecified(chars={'+', '-'})
	public TokenFactory pm;
	
	@CharSpecified(chars={'E'})
	public TokenFactory e;
	
	@CharSpecified(chars={'.'})
	public TokenFactory dot;
	
	@CharSpecified(chars={'+'})
	public TokenFactory plus;
	
	@CharSpecified(chars={'m'})
	public TokenFactory minus;
	
	@CharSpecified(chars={'^'})
	public TokenFactory caret;

	@CharSpecified(ranges={'a', 'z'})
	public TokenFactory name;

	@Rule("{name:',':'(':')'}")
	public TokenFactory params;
		
	@TokenType(TLocal.class)
	@Rule("name, [params]")
	public TokenFactory local;

	@TokenType(TObject.class)
	@Rule("name, {('.', name)}, [params]")
	public TokenFactory object;
	
	@TokenType(TNumber.class)
	@Rule("[pm], ([intlit], [(dot, intlit)]), [e, [pm], intlit]")
	public TokenFactory number;
	
	@TokenType(TGlobal.class)
	@Rule("caret, local")
	public TokenFactory global;
	
	@Rule("object | local | global | number")
	public TokenFactory expratom;

	@Choice({"plus", "minus"})
	public TokenFactory operator;

	@Rule("expratom, [operator, expr]")
	public TokenFactory expr;
}
