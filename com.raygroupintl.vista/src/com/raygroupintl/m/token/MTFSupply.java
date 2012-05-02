package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.TokenAdapter;
import com.raygroupintl.bnf.annotation.Adapter;
import com.raygroupintl.bnf.annotation.CChoice;
import com.raygroupintl.bnf.annotation.Parser;
import com.raygroupintl.bnf.annotation.Sequence;
import com.raygroupintl.bnf.annotation.Choice;
import com.raygroupintl.bnf.annotation.List;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class MTFSupply {
	public static class IndirectionAdapter implements TokenAdapter {
		@Override
		public IToken convert(IToken[] tokens) {
			return new TIndirection(tokens);
		}		
	}
	public static class LvnAdapter implements TokenAdapter {
		@Override
		public IToken convert(IToken[] tokens) {
			return new TLocal(tokens);
		}		
	}
	public static class GvnAdapter implements TokenAdapter {
		@Override
		public IToken convert(IToken[] tokens) {
			return new TGlobalNamed(tokens);
		}
	}
	public static class GvnNakedAdapter implements TokenAdapter {
		@Override
		public IToken convert(IToken[] tokens) {
			return new TGlobalNaked(tokens);
		}
	}
		
	static final Map<String, TokenAdapter> ADAPTERS = new HashMap<String, TokenAdapter>();
	static {
		ADAPTERS.put("actuallist", new TokenAdapter() {
			@Override
			public IToken convert(IToken[] tokens) {
				TList list = (tokens[1] == null) ? new TList() : (TList) tokens[1];
				return new TActualList(list);
			}
		});			 
		ADAPTERS.put("exprinpar", new TokenAdapter() {
			@Override
			public IToken convert(IToken[] tokens) {
				if (tokens[1] == null) {
					return new TInParantheses(new TEmpty());
				} else {
					return new TInParantheses(tokens[1]);
				}
			}
		});			 
	}

	public ITokenFactory dot = TFChar.DOT;
	public ITokenFactory comma = TFChar.COMMA;
	public ITokenFactory pipe = new TFConstChar('|');
	public ITokenFactory lsqr = new TFConstChar('[');
	public ITokenFactory rsqr = new TFConstChar(']');
	public ITokenFactory lpar = new TFConstChar('(');
	public ITokenFactory rpar = new TFConstChar(')');
	public ITokenFactory qmark = new TFConstChar('?');
	public ITokenFactory at = new TFConstChar('@');
	public ITokenFactory eq = new TFConstChar('=');
	public ITokenFactory caret = new TFConstChar('^');
	public ITokenFactory colon = new TFConstChar(':');

	public ITokenFactory nqmark = new TFConstString("'?");
	public ITokenFactory atlpar = new TFConstString("@(");
	public ITokenFactory caretquest = new TFConstString("^$");
	public ITokenFactory ddollar = new TFConstString("$$");

	public ITokenFactory ecomma = TFEmptyVerified.getInstance(',');
	public ITokenFactory erpar = TFEmptyVerified.getInstance(')');

	public ITokenFactory name = TFName.getInstance();
	public ITokenFactory ident = TFIdent.getInstance();
	public ITokenFactory numlit = TFNumLit.getInstance();
	public ITokenFactory operator = TFOperator.getInstance();
	public ITokenFactory error = TFSyntaxError.getInstance();
	public ITokenFactory unaryop = new TFConstChars("+-\'");
	public ITokenFactory strlit = new TFStringLiteral();
	
	@Sequence(value={"pipe", "expr", "pipe"}, required="all")
	public ITokenFactory env_0;
	@List(value="expratom", delim="comma", left="lsqr", right="rsqr")
	public ITokenFactory env_1;
	@Choice({"env_0", "env_1"})
	public ITokenFactory environment;
	
	@Sequence(value={"qmark", "pattern"}, required="all")
	public ITokenFactory exprtail_s0;
	@Sequence(value={"nqmark", "pattern"}, required="all")
	public ITokenFactory exprtail_s1;
	@Sequence(value={"operator", "expratom"}, required="all")
	public ITokenFactory exprtail_s2;
	@Choice({"exprtail_s0", "exprtail_s1", "exprtail_s2"})
	public ITokenFactory exprtail_s;
	@List("exprtail_s")
	public ITokenFactory exprtail;

	@List(value="expr", delim="comma")
	public ITokenFactory exprlist;
	
	@List(value="expr", delim="comma", left="lpar", right="rpar")
	public ITokenFactory exprlistinparan;

	
	@Sequence(value={"lpar", "expr", "rpar"}, required="all")
	public ITokenFactory exprinpar;
			
	@Sequence(value={"at", "expratom"}, required="all")
	public ITokenFactory indirection_0;
	@Sequence(value={"atlpar", "exprlist", "rpar"}, required="all")
	public ITokenFactory indirection_1;
	@Adapter("com.raygroupintl.m.token.MTFSupply$IndirectionAdapter")
	@Sequence(value={"indirection_0", "indirection_1"}, required="ro")
	public ITokenFactory indirection;
	
	@CChoice(value={"lvn", "gvnall", "indirection"}, preds={"idstart", "^", "@"})
	public ITokenFactory glvn;
	
	@Sequence(value={"environment", "name", "exprlistinparan"}, required="oro")
	public ITokenFactory gvn_0;
	@Adapter("com.raygroupintl.m.token.MTFSupply$GvnAdapter")	
	@Sequence(value={"caret", "gvn_0"}, required="all")
	public ITokenFactory gvn;
	
	@Sequence(value={"caretquest", "ident", "exprlistinparan"}, required="all")
	public ITokenFactory gvnssvn;

	@Sequence(value={"ddollar", "doargument"}, required="all")
	public ITokenFactory extrinsic;
	
	@Sequence(value={"unaryop", "expratom"}, required="all")
	public ITokenFactory unaryexpritem;
			
	@Adapter("com.raygroupintl.m.token.MTFSupply$GvnNakedAdapter")	
	@Sequence(value={"caret", "exprlistinparan"}, required="all")
	public ITokenFactory gvnnaked;
	
	@Sequence(value={"expr", "colon", "expr"}, required="all")
	public ITokenFactory dselectarg_e;
	@List(value="dselectarg_e", delim="comma")
	public ITokenFactory dselectarg;
	
	@CChoice(value={"gvnssvn", "gvnnaked", "gvn", "gvn"}, preds={"$", "(", "%|[", "letter"}, lead="^", def="error")
	public ITokenFactory gvnall;

	@CChoice(value={"extrinsic", "external", "intrinsic"}, preds={"$", "&", "letter"}, lead="$")
	public ITokenFactory expritem_d;
	
	@CChoice(value={"strlit", "expritem_d", "unaryexpritem", "numlit", "exprinpar", "numlit"}, preds={"\"", "$", "'+-", ".", "(", "digit"})
	public ITokenFactory expritem;
	
	@Sequence(value={"dot", "name"}, required="all")
	public ITokenFactory actual_d1;
	@Sequence(value={"dot", "indirection"}, required="all")
	public ITokenFactory actual_d2;
	@CChoice(value={"numlit", "actual_d1", "actual_d2"}, preds={"digit", "idstart", "@"}, lead=".", def="error")
	public ITokenFactory actual_d;
	
	@CChoice(value={"actual_d", "ecomma", "erpar"}, preds={".", ",", ")"}, def="expr")
	public ITokenFactory actual;
	
	@Choice({"glvn", "expritem"})
	public ITokenFactory expratom;

	@Adapter("com.raygroupintl.m.token.MTFSupply$LvnAdapter")
	@Sequence(value={"name", "exprlistinparan"}, required="ro")
	public ITokenFactory lvn;
	
	@Sequence(value={"expratom", "exprtail"}, required="ro")
	public ITokenFactory expr;
	
	public ITokenFactory doargument;
	public ITokenFactory external;
	
	@List(value="actual", delim="comma")
	public ITokenFactory actuallist_i;	
	@Sequence(value={"lpar", "actuallist_i", "rpar"}, required="ror")
	public ITokenFactory actuallist;

	public ITokenFactory pattern;
	
	public ITokenFactory intrinsic;
	
	@Sequence(value={"eq", "expr"}, required="all")
	public ITokenFactory deviceparam_1;
	@Sequence(value={"expr", "deviceparam_1"}, required="ro")
	public ITokenFactory deviceparam;
	@List(value="deviceparam", delim="colon", left="lpar", right="rpar", empty=true)
	public ITokenFactory deviceparams_i;
	@CChoice(value={"deviceparams_i"}, def="deviceparam", preds={"("})
	public ITokenFactory deviceparams;
	
	@Choice({"indirection", "name"})
	public ITokenFactory cmdkexcarg;
	@List(value="cmdkexcarg", delim="comma", left="lpar", right="rpar")
	public ITokenFactory cmdkexcargs;
	@CChoice(value={"cmdkexcargs", "indirection"}, preds={"(", "@"}, def="glvn")
	public ITokenFactory cmdkarg;
	@List(value="cmdkarg", delim="comma")
	public ITokenFactory cmdkargs;

	@Sequence(value={"colon", "expr"}, required="all")
	public ITokenFactory postcondition;
	
	public static class Std95Supply extends MTFSupply {	
		private MVersion version = MVersion.ANSI_STD_95;

		public ITokenFactory doargument = TFDoArgument.getInstance(this.version, true);
		public ITokenFactory external = new TFExternal(this.version);
		
		public ITokenFactory pattern = TFPattern.getInstance(this.version);
		
		public ITokenFactory intrinsic = new TFIntrinsic(this.version);
	}

	public static class CacheSupply extends MTFSupply {
		public static class ObjTailAdapter implements TokenAdapter {
			@Override
			public IToken convert(IToken[] tokens) {					
				return new TObjectTail(tokens);
			}
		}
		public static class LvnAdapter implements TokenAdapter {
			@Override
			public IToken convert(IToken[] tokens) {
				if ((tokens[1] != null) && (tokens[1] instanceof TObjectTail)) {					
					return new TObjectExpr(tokens);
				} else {					
					return new TLocal(tokens);
				}
			}		
		}
					
		private MVersion version = MVersion.CACHE;

		@Choice({"glvn", "expritem", "classmethod"})
		public ITokenFactory expratom;
		
		@Sequence(value={"dot", "name"}, required="all")
		public ITokenFactory lvn_objtail_ms;
		@List("lvn_objtail_ms")
		public ITokenFactory lvn_objtail_m;
		@Adapter("com.raygroupintl.m.token.MTFSupply$CacheSupply$ObjTailAdapter")
		@Sequence(value={"lvn_objtail_m", "actuallist"}, required="ro")
		public ITokenFactory lvn_objtail;
		@Choice(value={"exprlistinparan", "lvn_objtail"})
		public ITokenFactory lvn_next;
		@Adapter("com.raygroupintl.m.token.MTFSupply$CacheSupply$LvnAdapter")
		@Sequence(value={"name", "lvn_next"}, required="ro")
		public ITokenFactory lvn;
		
		public ITokenFactory doargument = TFDoArgument.getInstance(this.version, true);
		public ITokenFactory external = new TFExternal(this.version);
			
		@Choice({"expratom", "classmethod"})
		public ITokenFactory expr_0;
		@Sequence(value={"expr_0", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
		public ITokenFactory pattern = TFPattern.getInstance(this.version);
		
		public ITokenFactory ppclass = new TFConstString("##class");
		@Sequence(value={"dot", "name"}, required="all")
		public ITokenFactory classreftail;
		@List("classreftail")
		public ITokenFactory classreftaillst;
		@Sequence(value={"name", "classreftaillst"}, required="ro")
		public ITokenFactory classref;
		@Sequence(value={"ppclass", "lpar", "classref", "rpar", "dot", "name", "actuallist"}, required="all")
		public ITokenFactory classmethod;
		
		public ITokenFactory intrinsic = new TFIntrinsic(this.version);
	}
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
		
	public static MTFSupply getInstance(MVersion version) {
		try {
			switch (version) {
				case CACHE: {
					if (CACHE_SUPPLY == null) {
						CACHE_SUPPLY = Parser.parse(CacheSupply.class, CacheSupply.ADAPTERS);
						TFIntrinsic.initialize(version);
					}
					return CACHE_SUPPLY;
				}
				case ANSI_STD_95: {
					if (STD_95_SUPPLY == null) {
						STD_95_SUPPLY = Parser.parse(Std95Supply.class, Std95Supply.ADAPTERS);
						TFIntrinsic.initialize(version);
					}
					return STD_95_SUPPLY;
				}
				default:
					return null;
			}
		} catch (Throwable t) {
			return null;
		}
	}	
}
