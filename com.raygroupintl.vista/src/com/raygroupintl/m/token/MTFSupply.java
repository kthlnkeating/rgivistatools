package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFBasic;
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
import com.raygroupintl.bnf.annotation.Characters;
import com.raygroupintl.bnf.annotation.Equivalent;
import com.raygroupintl.bnf.annotation.Parser;
import com.raygroupintl.bnf.annotation.Sequence;
import com.raygroupintl.bnf.annotation.Choice;
import com.raygroupintl.bnf.annotation.List;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;

public abstract class MTFSupply {
	@Adapter("indirection")
	public static class IndirectionAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			return new TIndirection(tokens);
		}		
	}
	@Adapter("lvn")	
	public static class LvnAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			return new TLocal(tokens);
		}		
	}
	@Adapter("gvn")	
	public static class GvnAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			return new TGlobalNamed(tokens);
		}
	}
	@Adapter("gvnnaked")	
	public static class GvnNakedAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			return new TGlobalNaked(tokens);
		}
	}
	@Adapter("actuallist")	
	public static class ActualListAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			TList list = (tokens[1] == null) ? new TList() : (TList) tokens[1];
			return new TActualList(list);
		}
	}
	@Adapter("numlit")	
	public static class NumLitAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			String value = (new TArray(tokens)).getStringValue();
			return new TNumLit(value);
		}
	}
	@Adapter("labelref")	
	public static class LabelRefAdapter implements TokenAdapter {
		@Override
		public IToken convert(String line, int fromIndex,IToken[] tokens) {
			return new TLabelRef(tokens);
		}
	}
		
	public ITokenFactory dot = TFChar.DOT;
	public ITokenFactory comma = TFChar.COMMA;
	public ITokenFactory squote = new TFConstChar('\'');
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
	public ITokenFactory plus = new TFConstChar('+');
	public ITokenFactory pound = new TFConstChar('#');
	public ITokenFactory asterix = new TFConstChar('*');
	public ITokenFactory e = new TFConstChar('E');

	public ITokenFactory nqmark = new TFConstString("'?");
	public ITokenFactory atlpar = new TFConstString("@(");
	public ITokenFactory caretquest = new TFConstString("^$");
	public ITokenFactory ddollar = new TFConstString("$$");
	public ITokenFactory pm = new TFConstChars("+-");
	
	public ITokenFactory ecomma = TFEmptyVerified.getInstance(',');
	public ITokenFactory erpar = TFEmptyVerified.getInstance(')');

	@Characters(ranges={'a', 'z', 'A', 'Z'}, excludechars={'y', 'Y'})
	public ITokenFactory identxy;
	@Characters(ranges={'a', 'y', 'A', 'Y'})
	public ITokenFactory identxz;
	public ITokenFactory yy = new TFConstChars("zZ");
	public ITokenFactory zz = new TFConstChars("zZ");
	@Characters(ranges={'a', 'x', 'A', 'X'})
	public ITokenFactory paton;
	@Sequence(value={"yy", "identxy", "yy"}, required="all")
	public ITokenFactory patony;
	@Sequence(value={"zz", "identxz", "zz"}, required="all")
	public ITokenFactory patonz;
	@Choice({"paton", "patony", "patonz"})
	public ITokenFactory patons;
	@Sequence(value={"squote", "patons"}, required="or")
	public ITokenFactory patcode;	
	@Sequence(value={"intlit", "dot", "intlit"})
	public ITokenFactory repcount;
	@CChoice(value={"alternation", "patcode", "strlit"}, preds={"(", "letter", "\""})
	public ITokenFactory patatom_re;
	@Sequence(value={"repcount", "patatom_re"}, required="all")
	public ITokenFactory patatom;	
	@List(value="patatoms", delim="comma", left="lpar", right="rpar")	
	public ITokenFactory alternation;	
	@List(value="patatom")	
	public ITokenFactory patatoms;
	@Choice({"indirection", "patatoms"})
	public ITokenFactory pattern;
	

	
	public ITokenFactory name = TFName.getInstance();
	public ITokenFactory ident = TFIdent.getInstance();
	public ITokenFactory intlit = new TIntLit.Factory();
	@Choice({"name", "intlit"})
	public ITokenFactory label;
	
	@Sequence(value={"caret", "environment", "name"}, required="ror")
	public ITokenFactory envroutine;
	@Sequence(value={"name", "envroutine"})
	public ITokenFactory labelref;
	
	
	@Sequence(value={"e", "pm", "intlit"}, required="ror")
	public ITokenFactory exp;
	@Sequence(value={"dot", "intlit"}, required="all")
	public ITokenFactory mantista_1;
	@Sequence(value={"intlit", "mantista_1"})
	public ITokenFactory mantista;
	@Sequence(value={"pm", "mantista", "exp"}, required="oro")
	public ITokenFactory numlit;
		
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
	
	@Sequence(value={"eq", "expr"}, required="all")
	public ITokenFactory eqexpr;
			
	@Sequence(value={"at", "expratom"}, required="all")
	public ITokenFactory indirection_0;
	@Sequence(value={"atlpar", "exprlist", "rpar"}, required="all")
	public ITokenFactory indirection_1;
	@Sequence(value={"indirection_0", "indirection_1"}, required="ro")
	public ITokenFactory indirection;
	
	@CChoice(value={"lvn", "gvnall", "indirection"}, preds={"idstart", "^", "@"})
	public ITokenFactory glvn;
	
	@Sequence(value={"environment", "name", "exprlistinparan"}, required="oro")
	public ITokenFactory gvn_0;
	@Sequence(value={"caret", "gvn_0"}, required="all")
	public ITokenFactory gvn;
	
	@Sequence(value={"caretquest", "ident", "exprlistinparan"}, required="all")
	public ITokenFactory gvnssvn;

	@Sequence(value={"ddollar", "extrinsicarg"}, required="all")
	public ITokenFactory extrinsic;
	
	@Sequence(value={"unaryop", "expratom"}, required="all")
	public ITokenFactory unaryexpritem;
			
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

	@Sequence(value={"name", "exprlistinparan"}, required="ro")
	public ITokenFactory lvn;
	
	@Sequence(value={"expratom", "exprtail"}, required="ro")
	public ITokenFactory expr;
	
	public ITokenFactory external;
	
	@List(value="actual", delim="comma")
	public ITokenFactory actuallist_i;	
	@Sequence(value={"lpar", "actuallist_i", "rpar"}, required="ror")
	public ITokenFactory actuallist;

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

	@Sequence(value={"glvn", "eqexpr"}, required="all")
	public ITokenFactory cmdmarg_basic;
	@Sequence(value={"indirection", "eqexpr"}, required="ro")
	public ITokenFactory cmdmarg_indirect;
	@Choice({"cmdmarg_indirect", "cmdmarg_basic"})
	public ITokenFactory cmdmarg;
	@List(value="cmdmarg", delim="comma")
	public ITokenFactory cmdmargs;
	
	@Choice({"exprlistinparan", "expr"})
	public ITokenFactory exprorinlist;
	@Sequence(value={"colon", "deviceparams", "colon", "expr", "colon", "exprorinlist"}, required="rooooo")
	public ITokenFactory cmdoarg_tail;
	@Sequence(value={"expr", "cmdoarg_tail"}, required="ro")
	public ITokenFactory cmdoarg_basic;
	@Choice({"indirection", "cmdoarg_basic"})
	public ITokenFactory cmdoarg;
	@List(value="cmdoarg", delim="comma")
	public ITokenFactory cmdoargs;
		
	@Choice({"indirection", "label"})
	public ITokenFactory linetagname;
	@Sequence(value={"plus", "expr"}, required="all")
	public ITokenFactory lineoffset;
	@Sequence(value={"linetagname", "lineoffset"})
	public ITokenFactory tagspec;
	@Sequence(value={"environment", "name"}, required="or")
	public ITokenFactory envname;
	@Choice(value={"rindirection", "envname"})
	public ITokenFactory routinespec_0;
	@Sequence(value={"caret", "routinespec_0"}, required="all")
	public ITokenFactory routinespec;
	@Sequence({"tagspec", "routinespec"})
	public ITokenFactory cmdgargmain;
	@Sequence(value={"cmdgargmain", "postcondition"}, required="ro")
	public ITokenFactory cmdgarg;
	@List(value="cmdgarg", delim="comma")
	public ITokenFactory cmdgargs;
	
	
	
	@Sequence(value={"pound", "expr"}, required="all")
	public ITokenFactory readcount;

	@Sequence(value={"qmark", "expr"}, required="all")
	public ITokenFactory tabformat;
	public ITokenFactory excorpounds = TFBasic.getInstance('!','#');
	@Sequence(value={"excorpounds", "tabformat"}, required="ro")
	public ITokenFactory xtabformat;
	@Choice({"tabformat", "xtabformat"})
	public ITokenFactory format;
	
	@Sequence(value={"glvn", "readcount", "timeout"}, required="roo")
	public ITokenFactory cmdrarg_def;	
	@Sequence(value={"asterix", "glvn", "timeout"}, required="rro")
	public ITokenFactory cmdrarg_ast;	
	@Sequence(value={"indirection", "timeout"}, required="ro")
	public ITokenFactory cmdrarg_at;	
	@CChoice(value={"format", "strlit", "cmdrarg_ast", "cmdrarg_at"}, preds={"!#?/", "\"", "*", "@"}, def="cmdrarg_def")
	public ITokenFactory cmdrarg;
	@List(value="cmdrarg", delim="comma")
	public ITokenFactory cmdrargs;
		
	@Sequence(value={"colon", "expr"}, required="all")
	public ITokenFactory postcondition;
	
	@Sequence(value={"colon", "expr"}, required="all")
	public ITokenFactory timeout;
	

	@List(value="expr", delim="colon", left="lpar", right="rpar", empty=true)
	public ITokenFactory usedeviceparam_list;
	@Choice(value={"usedeviceparam_list", "expr"})
	public ITokenFactory usedeviceparam;
	@Sequence(value={"colon", "usedeviceparam"}, required="ro")
	public ITokenFactory colonusedeviceparam;
	@Sequence(value={"expr", "colonusedeviceparam", "colonusedeviceparam"}, required="roo")
	public ITokenFactory cmduarg;
	@List(value="cmduarg", delim="comma")
	public ITokenFactory cmduargs;
	
	@Sequence(value={"at", "expratom"}, required="all")
	public ITokenFactory rindirection;
	@Sequence(value={"label", "lineoffset"}, required="ro")
	public ITokenFactory labelwoffset;
	@Choice({"rindirection", "labelwoffset"})
	public ITokenFactory entryspec_0;
	@Sequence(value={"entryspec_0", "routinespec", "actuallist", "colonusedeviceparam", "timeout"}, required="ooooo")
	public ITokenFactory cmdjarg;
	@Sequence(value={"colon", "usedeviceparam"}, required="ro")
	public ITokenFactory jobparams;
	@List(value="cmdjarg", delim="comma")
	public ITokenFactory cmdjargs;
	
	@Sequence(value={"label", "lineoffset"}, required="ro")
	public ITokenFactory dlabelwoffset;
	@Choice({"rindirection", "dlabelwoffset"})
	public ITokenFactory dentryspec_0;	

	@Sequence(value={"labelpiece", "lineoffset", "doroutinef", "actuallist"}, required="oooo")
	public ITokenFactory extrinsicarg;
	@Sequence(value={"labelpiece", "lineoffset", "doroutinef", "actuallist", "postcondition"}, required="ooooo")
	public ITokenFactory cmddarg;
	@List(value="cmddarg", delim="comma")
	public ITokenFactory cmddargs;

	@Sequence(value={"environment", "name"}, required="or")
	public ITokenFactory doroutine;
	@Choice({"rindirection", "doroutine"})
	public ITokenFactory doroutineind;
	@Sequence(value={"caret", "doroutineind"}, required="ro")
	public ITokenFactory doroutinef;
	
	@Choice(value={"indirection", "label"})
	public ITokenFactory labelpiece;
	
	@Choice(value={"indirection", "intrinsic", "glvn"})
	public ITokenFactory setlhsbasic;
	@List(value="setlhsbasic", delim="comma", left="lpar", right="rpar")
	public ITokenFactory setlhsbasics;
	@Choice(value={"setlhsbasics", "setlhsbasic"})
	public ITokenFactory setlhs;
	@Equivalent("expr")
	public ITokenFactory setrhs;
	@Sequence(value={"setlhs", "eq", "setrhs"}, required="all")
	public ITokenFactory setarg_direct;
	@Sequence(value={"indirection", "eq", "setrhs"}, required="roo")
	public ITokenFactory setarg_indirect;
	@Choice(value={"setarg_indirect", "setarg_direct"})
	public ITokenFactory setarg;
	@List(value="setarg", delim="comma")
	public ITokenFactory setargs;
	
	@Sequence(value={"colon", "deviceparams"}, required="all")
	public ITokenFactory closearg_dp;
	@Sequence(value={"expr", "closearg_dp"}, required="ro")
	public ITokenFactory closearg_direct;
	@Choice(value={"indirection", "closearg_direct"})
	public ITokenFactory closearg;
	@List(value="closearg", delim="comma")
	public ITokenFactory closeargs;
	
	@Sequence(value={"colon", "expr"}, required="all")
	public ITokenFactory cexpr;
	@Sequence(value={"expr", "cexpr", "cexpr"}, required="roo")
	public ITokenFactory forrhs;
	@List(value="forrhs", delim="comma")
	public ITokenFactory forrhss;
	@Sequence(value={"lvn", "eq", "forrhss"}, required="all")
	public ITokenFactory forarg;
	
	@CChoice(value={"gvn", "indirection"}, preds={"^", "@"}, def="lvn")
	public ITokenFactory lockee_single;
	@List(value="lockee", delim="comma", left="lpar", right="rpar")
	public ITokenFactory lockee_list;
	@Choice({"lockee_single", "lockee_list"})
	public ITokenFactory lockee;
	@Sequence(value={"pm", "lockee", "timeout"}, required="oro")
	public ITokenFactory lockarg;
	@List(value="lockarg", delim="comma")
	public ITokenFactory lockargs;
	
	@List(value="lvn", delim="comma", left="lpar", right="rpar")
	public ITokenFactory lvns;
	@CChoice(value={"lvns", "indirection", "intrinsic"}, preds={"(", "@", "$"}, def="name")
	public ITokenFactory newarg;
	@List(value="newarg", delim="comma")
	public ITokenFactory newargs;
		
	public static class Std95Supply extends MTFSupply {	
		private MVersion version = MVersion.ANSI_STD_95;

		public ITokenFactory external = new TFExternal(this.version);
		
		public ITokenFactory intrinsic = new TFIntrinsic(this.version);
	}

	public static class CacheSupply extends MTFSupply {
		@Adapter("lvn_objtail")
		public static class ObjTailAdapter implements TokenAdapter {
			@Override
			public IToken convert(String line, int fromIndex,IToken[] tokens) {					
				return new TObjectTail(tokens);
			}
		}
		@Adapter("lvn")
		public static class LvnAdapter implements TokenAdapter {
			@Override
			public IToken convert(String line, int fromIndex,IToken[] tokens) {
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
		@Sequence(value={"lvn_objtail_m", "actuallist"}, required="ro")
		public ITokenFactory lvn_objtail;
		@Choice(value={"exprlistinparan", "lvn_objtail"})
		public ITokenFactory lvn_next;
		@Sequence(value={"name", "lvn_next"}, required="ro")
		public ITokenFactory lvn;
		
		public ITokenFactory external = new TFExternal(this.version);
			
		@Choice({"expratom", "classmethod"})
		public ITokenFactory expr_0;
		@Sequence(value={"expr_0", "exprtail"}, required="ro")
		public ITokenFactory expr;
		
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
		
		public ITokenFactory system = new TFConstString("$SYSTEM", true);
		@Sequence(value={"dot", "name"}, required="all")
		public ITokenFactory method;
		@List(value="method")
		public ITokenFactory methods;
		@Sequence(value={"system", "methods", "actuallist"}, required="ror")
		public ITokenFactory systemcall;
		
		@Sequence(value={"label", "method"}, required="ro")
		public ITokenFactory labelpiece_0;
		@CChoice(value={"indirection", "classmethod", "systemcall"}, preds={"@", "#", "$"}, def="labelpiece_0")
		public ITokenFactory labelpiece;
	
		@Choice({"methods", "lineoffset"})
		public ITokenFactory dlabelwoffset_1;
		@Sequence(value={"label", "dlabelwoffset_1"}, required="ro")
		public ITokenFactory dlabelwoffset;		
		@Choice({"indirection", "systemcall", "classmethod", "dlabelwoffset"})
		public ITokenFactory dentryspec_0;
	
		@Sequence(value={"environment", "name", "method"}, required="oro")
		public ITokenFactory doroutine;

		@Choice({"classmethod", "expr"})
		public ITokenFactory setrhs;

	}
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
		
	public static MTFSupply getInstance(MVersion version) {
		try {
			switch (version) {
				case CACHE: {
					if (CACHE_SUPPLY == null) {
						CACHE_SUPPLY = Parser.parse(CacheSupply.class);
						TFIntrinsic.initialize(version);
					}
					return CACHE_SUPPLY;
				}
				case ANSI_STD_95: {
					if (STD_95_SUPPLY == null) {
						STD_95_SUPPLY = Parser.parse(Std95Supply.class);
						
						
						
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
