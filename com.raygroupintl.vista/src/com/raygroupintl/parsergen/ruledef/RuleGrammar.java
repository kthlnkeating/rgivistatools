//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.DelimitedListTokenType;
import com.raygroupintl.parsergen.SequenceTokenType;
import com.raygroupintl.parsergen.TokenType;

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
	@Choice({"escapedsquote", "escapedn", "escapedr", "escapedt", "noquote"})
	public TokenFactory squoted;

	@CharSpecified(chars={'"'}, single=true)
	public TokenFactory quote;
	@CharSpecified(excludechars={'"'})
	public TokenFactory quoted;

	@CharSpecified(chars={'|'}, single=true)
	public TokenFactory pipe;
	
	@CharSpecified(chars={'+', '-'}, single=true)
	public TokenFactory pm;
	
	@CharSpecified(chars={'1', '0'}, single=true)
	public TokenFactory bool;
	
	@WordSpecified("...")
	public TokenFactory ellipsis;
	@WordSpecified("\\'")
	public TokenFactory escapedsquote;
	@WordSpecified("\\n")
	public TokenFactory escapedn;
	@WordSpecified("\\r")
	public TokenFactory escapedr;
	@WordSpecified("\\t")
	public TokenFactory escapedt;
	@CharSpecified(excludechars={'\''}, single=true)
	public TokenFactory noquote;

	@TokenType(TSymbol.class)
	@CharSpecified(ranges={'a', 'z'})
	public TokenFactory specifiedsymbol; 

	@SequenceTokenType(TCharSymbol.class)
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
	@SequenceTokenType(TCharSymbol.class)
	@Sequence(value={"dpm", "charsymbolwr", "charsymbollist"}, required="oro")
	public TokenFactory charsymbolall; 

	@SequenceTokenType(TConstSymbol.class)
	@Sequence(value={"quote", "quoted", "quote", "colon", "bool"}, required="rrroo")
	public TokenFactory constsymbol; 
	
	@Choice({"specifiedsymbol", "charsymbolall", "constsymbol", "optionalsymbols", "requiredsymbols", "list"})
	public TokenFactory symbol; 
	
	@DelimitedListTokenType(TChoiceOfSymbols.class)
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
	
	@SequenceTokenType(TOptionalSymbols.class)
	@Sequence(value={"openoptional", "sequence", "closeoptional"}, required="all")
	public TokenFactory optionalsymbols; 
	@SequenceTokenType(TRequiredSymbols.class)
	@Sequence(value={"openrequired", "sequence", "closerequired"}, required="all")
	public TokenFactory requiredsymbols;
	
	@Sequence(value={"colon", "anysymbols", "colon", "anysymbols", "colon", "bool", "colon", "bool"}, required="rrrroooo")
	public TokenFactory leftrightspec;
	@Sequence(value={"colon", "anysymbols", "leftrightspec"}, required="roo")
	public TokenFactory delimleftrightspec;
	@SequenceTokenType(TSymbolList.class)
	@Sequence(value={"openlist", "symbolchoice", "delimleftrightspec", "closelist"}, required="rror")
	public TokenFactory list;
	
	@Choice({"specifiedsymbol", "charsymbolall", "constsymbol"})
	public TokenFactory anysymbols;
	
	@DelimitedListTokenType(TSymbolSequence.class)
	@List(value="symbolchoice", delim="delimiter")
	public TokenFactory sequence;
}
