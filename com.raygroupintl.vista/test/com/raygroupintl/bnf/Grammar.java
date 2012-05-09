package com.raygroupintl.bnf;

import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.annotation.Characters;
import com.raygroupintl.bnf.annotation.Choice;
import com.raygroupintl.bnf.annotation.Description;
import com.raygroupintl.bnf.annotation.TokenType;

public class Grammar {
	@Characters(ranges={'0', '9'})
	public TokenFactory intlit;

	@Characters(chars={'+', '-'})
	public TokenFactory pm;
	
	@Characters(chars={'E'})
	public TokenFactory e;
	
	@Characters(chars={'.'})
	public TokenFactory dot;
	
	@Characters(chars={'+'})
	public TokenFactory plus;
	
	@Characters(chars={'m'})
	public TokenFactory minus;
	
	@Characters(chars={'^'})
	public TokenFactory caret;

	@Characters(ranges={'a', 'z'})
	public TokenFactory local;

	@TokenType(TNumber.class)
	@Description("[pm], ([intlit], [(dot, intlit)]), [e, [pm], intlit]")
	public TokenFactory number;
	
	@TokenType(TGlobal.class)
	@Description("caret, local")
	public TokenFactory global;
	
	@Choice({"local", "global", "number"})
	public TokenFactory expratom;

	@Choice({"plus", "minus"})
	public TokenFactory operator;

	@Description("expratom, [operator, expr]")
	public TokenFactory expr;
	
	
	
}
