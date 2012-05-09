package com.raygroupintl.m.token;

import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFConstString;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSyntaxError;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.SequenceAdapter;
import com.raygroupintl.bnf.annotation.Adapter;
import com.raygroupintl.bnf.annotation.CChoice;
import com.raygroupintl.bnf.annotation.Characters;
import com.raygroupintl.bnf.annotation.Equivalent;
import com.raygroupintl.bnf.annotation.Parser;
import com.raygroupintl.bnf.annotation.Sequence;
import com.raygroupintl.bnf.annotation.Choice;
import com.raygroupintl.bnf.annotation.List;
import com.raygroupintl.vista.struct.MError;

public class MTFSupply {
	public static class IndirectionAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TIndirection(tokens);
		}		
	}
	public static class LvnAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TLocal(tokens);
		}		
	}
	public static class GvnAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TGlobalNamed(tokens);
		}
	}
	public static class GvnNakedAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TGlobalNaked(tokens);
		}
	}
	public static class ActualListAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			TList list = (tokens[1] == null) ? new TList() : (TList) tokens[1];
			return new TActualList(list);
		}
	}
	public static class NumLitAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			String value = (new TArray(tokens)).getStringValue();
			return new TNumLit(value);
		}
	}
	public static class LabelRefAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TLabelRef(tokens);
		}
	}
	public static class LineAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			return new TLine(tokens);
		}
	}
		
	public TokenFactory dot = TFChar.DOT;
	public TokenFactory comma = TFChar.COMMA;
	public TokenFactory squote = new TFConstChar('\'');
	public TokenFactory pipe = new TFConstChar('|');
	public TokenFactory lsqr = new TFConstChar('[');
	public TokenFactory rsqr = new TFConstChar(']');
	public TokenFactory lpar = new TFConstChar('(');
	public TokenFactory rpar = new TFConstChar(')');
	public TokenFactory qmark = new TFConstChar('?');
	public TokenFactory at = new TFConstChar('@');
	public TokenFactory eq = new TFConstChar('=');
	public TokenFactory caret = new TFConstChar('^');
	public TokenFactory colon = new TFConstChar(':');
	public TokenFactory plus = new TFConstChar('+');
	public TokenFactory pound = new TFConstChar('#');
	public TokenFactory asterix = new TFConstChar('*');
	public TokenFactory e = new TFConstChar('E');
	public TokenFactory slash = TFChar.SLASH;

	public TokenFactory nqmark = new TFConstString("'?");
	public TokenFactory atlpar = new TFConstString("@(");
	public TokenFactory caretquest = new TFConstString("^$");
	public TokenFactory ddollar = new TFConstString("$$");
	public TokenFactory dollar = new TFConstChar('$');
	public TokenFactory pm = new TFConstChars("+-");
	
	public TokenFactory ecomma = TFEmptyVerified.getInstance(',');
	public TokenFactory erpar = TFEmptyVerified.getInstance(')');

	@Characters(ranges={'a', 'z', 'A', 'Z'}, excludechars={'y', 'Y'})
	public TokenFactory identxy;
	@Characters(ranges={'a', 'y', 'A', 'Y'})
	public TokenFactory identxz;
	public TokenFactory yy = new TFConstChars("zZ");
	public TokenFactory zz = new TFConstChars("zZ");
	@Characters(ranges={'a', 'x', 'A', 'X'})
	public TokenFactory paton;
	@Sequence(value={"yy", "identxy", "yy"}, required="all")
	public TokenFactory patony;
	@Sequence(value={"zz", "identxz", "zz"}, required="all")
	public TokenFactory patonz;
	@Choice({"paton", "patony", "patonz"})
	public TokenFactory patons;
	@Sequence(value={"squote", "patons"}, required="or")
	public TokenFactory patcode;	
	@Sequence(value={"intlit", "dot", "intlit"})
	public TokenFactory repcount;
	@CChoice(value={"alternation", "patcode", "strlit"}, preds={"(", "letter", "\""})
	public TokenFactory patatom_re;
	@Sequence(value={"repcount", "patatom_re"}, required="all")
	public TokenFactory patatom;	
	@List(value="patatoms", delim="comma", left="lpar", right="rpar")	
	public TokenFactory alternation;	
	@List(value="patatom")	
	public TokenFactory patatoms;
	@Choice({"indirection", "patatoms"})
	public TokenFactory pattern;
	
	public TokenFactory name = TFName.getInstance();
	public TokenFactory ident = TFIdent.getInstance();
	public TokenFactory intlit = new TIntLit.Factory();
	@Choice({"name", "intlit"})
	public TokenFactory label;
	
	@Sequence(value={"caret", "environment", "name"}, required="ror")
	public TokenFactory envroutine;
	
	@Adapter(LabelRefAdapter.class)
	@Sequence(value={"name", "envroutine"})
	public TokenFactory labelref;
	
	
	@Sequence(value={"e", "pm", "intlit"}, required="ror")
	public TokenFactory exp;
	@Sequence(value={"dot", "intlit"}, required="all")
	public TokenFactory mantista_1;
	@Sequence(value={"intlit", "mantista_1"})
	public TokenFactory mantista;
	
	@Adapter(NumLitAdapter.class)
	@Sequence(value={"pm", "mantista", "exp"}, required="oro")
	public TokenFactory numlit;
		
	public TFOperator operator = new TFOperator();
	public TokenFactory error = new TFSyntaxError( MError.ERR_GENERAL_SYNTAX);
	public TokenFactory unaryop = new TFConstChars("+-\'");
	public TokenFactory strlit = new TFStringLiteral();
	
	@Sequence(value={"pipe", "expr", "pipe"}, required="all")
	public TokenFactory env_0;
	@List(value="expratom", delim="comma", left="lsqr", right="rsqr")
	public TokenFactory env_1;
	@Choice({"env_0", "env_1"})
	public TokenFactory environment;
	
	@Sequence(value={"qmark", "pattern"}, required="all")
	public TokenFactory exprtail_s0;
	@Sequence(value={"nqmark", "pattern"}, required="all")
	public TokenFactory exprtail_s1;
	@Sequence(value={"operator", "expratom"}, required="all")
	public TokenFactory exprtail_s2;
	@Choice({"exprtail_s0", "exprtail_s1", "exprtail_s2"})
	public TokenFactory exprtail_s;
	@List("exprtail_s")
	public TokenFactory exprtail;

	@List(value="expr", delim="comma")
	public TokenFactory exprlist;
	
	@List(value="expr", delim="comma", left="lpar", right="rpar")
	public TokenFactory exprlistinparan;

	@Sequence(value={"lpar", "expr", "rpar"}, required="all")
	public TokenFactory exprinpar;
	
	@Sequence(value={"eq", "expr"}, required="all")
	public TokenFactory eqexpr;
			
	@Sequence(value={"at", "expratom"}, required="all")
	public TokenFactory indirection_0;
	@Sequence(value={"atlpar", "exprlist", "rpar"}, required="all")
	public TokenFactory indirection_1;
	
	@Adapter(IndirectionAdapter.class)
	@Sequence(value={"indirection_0", "indirection_1"}, required="ro")
	public TokenFactory indirection;
	
	@CChoice(value={"lvn", "gvnall", "indirection"}, preds={"idstart", "^", "@"})
	public TokenFactory glvn;
	
	@Sequence(value={"environment", "name", "exprlistinparan"}, required="oro")
	public TokenFactory gvn_0;
	
	@Adapter(GvnAdapter.class)
	@Sequence(value={"caret", "gvn_0"}, required="all")
	public TokenFactory gvn;
	
	@Sequence(value={"caretquest", "ident", "exprlistinparan"}, required="all")
	public TokenFactory gvnssvn;

	@Sequence(value={"ddollar", "extrinsicarg"}, required="all")
	public TokenFactory extrinsic;
	
	@Sequence(value={"unaryop", "expratom"}, required="all")
	public TokenFactory unaryexpritem;
	
	@Adapter(GvnNakedAdapter.class)
	@Sequence(value={"caret", "exprlistinparan"}, required="all")
	public TokenFactory gvnnaked;
	
	@Sequence(value={"expr", "colon", "expr"}, required="all")
	public TokenFactory dselectarg_e;
	@List(value="dselectarg_e", delim="comma")
	public TokenFactory dselectarg;
	
	@CChoice(value={"gvnssvn", "gvnnaked", "gvn", "gvn"}, preds={"$", "(", "%|[", "letter"}, lead="^", def="error")
	public TokenFactory gvnall;

	@CChoice(value={"extrinsic", "external", "intrinsic"}, preds={"$", "&", "letter"}, lead="$")
	public TokenFactory expritem_d;
	
	@CChoice(value={"strlit", "expritem_d", "unaryexpritem", "numlit", "exprinpar", "numlit"}, preds={"\"", "$", "'+-", ".", "(", "digit"})
	public TokenFactory expritem;
	
	@Sequence(value={"dot", "name"}, required="all")
	public TokenFactory actual_d1;
	@Sequence(value={"dot", "indirection"}, required="all")
	public TokenFactory actual_d2;
	@CChoice(value={"numlit", "actual_d1", "actual_d2"}, preds={"digit", "idstart", "@"}, lead=".", def="error")
	public TokenFactory actual_d;
	
	@CChoice(value={"actual_d", "ecomma", "erpar"}, preds={".", ",", ")"}, def="expr")
	public TokenFactory actual;
	
	@Choice({"glvn", "expritem"})
	public TokenFactory expratom;

	@Adapter(LvnAdapter.class)
	@Sequence(value={"name", "exprlistinparan"}, required="ro")
	public TokenFactory lvn;
	
	@Sequence(value={"expratom", "exprtail"}, required="ro")
	public TokenFactory expr;
	
	@List(value="actual", delim="comma")
	public TokenFactory actuallist_i;	

	@Adapter(ActualListAdapter.class)
	@Sequence(value={"lpar", "actuallist_i", "rpar"}, required="ror")
	public TokenFactory actuallist;

	@Sequence(value={"eq", "expr"}, required="all")
	public TokenFactory deviceparam_1;
	@Sequence(value={"expr", "deviceparam_1"}, required="ro")
	public TokenFactory deviceparam;
	@List(value="deviceparam", delim="colon", left="lpar", right="rpar", empty=true)
	public TokenFactory deviceparams_i;
	@CChoice(value={"deviceparams_i"}, def="deviceparam", preds={"("})
	public TokenFactory deviceparams;
	
	@Choice({"indirection", "name"})
	public TokenFactory cmdkexcarg;
	@List(value="cmdkexcarg", delim="comma", left="lpar", right="rpar")
	public TokenFactory cmdkexcargs;
	@CChoice(value={"cmdkexcargs", "indirection"}, preds={"(", "@"}, def="glvn")
	public TokenFactory cmdkarg;
	@List(value="cmdkarg", delim="comma")
	public TokenFactory cmdkargs;

	@Sequence(value={"glvn", "eqexpr"}, required="all")
	public TokenFactory cmdmarg_basic;
	@Sequence(value={"indirection", "eqexpr"}, required="ro")
	public TokenFactory cmdmarg_indirect;
	@Choice({"cmdmarg_indirect", "cmdmarg_basic"})
	public TokenFactory cmdmarg;
	@List(value="cmdmarg", delim="comma")
	public TokenFactory cmdmargs;
	
	@Choice({"exprlistinparan", "expr"})
	public TokenFactory exprorinlist;
	@Sequence(value={"colon", "deviceparams", "colon", "expr", "colon", "exprorinlist"}, required="rooooo")
	public TokenFactory cmdoarg_tail;
	@Sequence(value={"expr", "cmdoarg_tail"}, required="ro")
	public TokenFactory cmdoarg_basic;
	@Choice({"indirection", "cmdoarg_basic"})
	public TokenFactory cmdoarg;
	@List(value="cmdoarg", delim="comma")
	public TokenFactory cmdoargs;
		
	@Choice({"indirection", "label"})
	public TokenFactory linetagname;
	@Sequence(value={"plus", "expr"}, required="all")
	public TokenFactory lineoffset;
	@Sequence(value={"linetagname", "lineoffset"})
	public TokenFactory tagspec;
	@Sequence(value={"environment", "name"}, required="or")
	public TokenFactory envname;
	@Choice(value={"rindirection", "envname"})
	public TokenFactory routinespec_0;
	@Sequence(value={"caret", "routinespec_0"}, required="all")
	public TokenFactory routinespec;
	@Sequence({"tagspec", "routinespec"})
	public TokenFactory cmdgargmain;
	@Sequence(value={"cmdgargmain", "postcondition"}, required="ro")
	public TokenFactory cmdgarg;
	@List(value="cmdgarg", delim="comma")
	public TokenFactory cmdgargs;
	
	@Sequence(value={"pound", "expr"}, required="all")
	public TokenFactory readcount;

	@Sequence(value={"qmark", "expr"}, required="all")
	public TokenFactory tabformat;
	public TokenFactory excorpounds = TFBasic.getInstance('!','#');
	@Sequence(value={"excorpounds", "tabformat"}, required="ro")
	public TokenFactory xtabformat;
	@Choice({"tabformat", "xtabformat"})
	public TokenFactory format;
	
	@Sequence(value={"glvn", "readcount", "timeout"}, required="roo")
	public TokenFactory cmdrarg_def;	
	@Sequence(value={"asterix", "glvn", "timeout"}, required="rro")
	public TokenFactory cmdrarg_ast;	
	@Sequence(value={"indirection", "timeout"}, required="ro")
	public TokenFactory cmdrarg_at;	
	@CChoice(value={"format", "strlit", "cmdrarg_ast", "cmdrarg_at"}, preds={"!#?/", "\"", "*", "@"}, def="cmdrarg_def")
	public TokenFactory cmdrarg;
	@List(value="cmdrarg", delim="comma")
	public TokenFactory cmdrargs;
		
	@Sequence(value={"colon", "expr"}, required="all")
	public TokenFactory postcondition;
	@Sequence(value={"asterix", "expr"}, required="all")
	public TokenFactory asterixexpr;
	
	@Sequence(value={"colon", "expr"}, required="all")
	public TokenFactory timeout;
	

	@List(value="expr", delim="colon", left="lpar", right="rpar", empty=true)
	public TokenFactory usedeviceparam_list;
	@Choice(value={"usedeviceparam_list", "expr"})
	public TokenFactory usedeviceparam;
	@Sequence(value={"colon", "usedeviceparam"}, required="ro")
	public TokenFactory colonusedeviceparam;
	@Sequence(value={"expr", "colonusedeviceparam", "colonusedeviceparam"}, required="roo")
	public TokenFactory cmduarg;
	@List(value="cmduarg", delim="comma")
	public TokenFactory cmduargs;
	
	@Sequence(value={"at", "expratom"}, required="all")
	public TokenFactory rindirection;
	@Sequence(value={"label", "lineoffset"}, required="ro")
	public TokenFactory labelwoffset;
	@Choice({"rindirection", "labelwoffset"})
	public TokenFactory entryspec_0;
	@Sequence(value={"entryspec_0", "routinespec", "actuallist", "colonusedeviceparam", "timeout"}, required="ooooo")
	public TokenFactory cmdjarg;
	@Sequence(value={"colon", "usedeviceparam"}, required="ro")
	public TokenFactory jobparams;
	@List(value="cmdjarg", delim="comma")
	public TokenFactory cmdjargs;
	
	@Sequence(value={"label", "lineoffset"}, required="ro")
	public TokenFactory dlabelwoffset;
	@Choice({"rindirection", "dlabelwoffset"})
	public TokenFactory dentryspec_0;	

	@Sequence(value={"labelpiece", "lineoffset", "doroutinef", "actuallist"}, required="oooo")
	public TokenFactory extrinsicarg;
	@Sequence(value={"labelpiece", "lineoffset", "doroutinef", "actuallist", "postcondition"}, required="ooooo")
	public TokenFactory cmddarg;
	@List(value="cmddarg", delim="comma")
	public TokenFactory cmddargs;

	@Sequence(value={"environment", "name"}, required="or")
	public TokenFactory doroutine;
	@Choice({"rindirection", "doroutine"})
	public TokenFactory doroutineind;
	@Sequence(value={"caret", "doroutineind"}, required="ro")
	public TokenFactory doroutinef;
	
	@Choice(value={"indirection", "label"})
	public TokenFactory labelpiece;
	
	@Choice(value={"indirection", "intrinsic", "glvn"})
	public TokenFactory setlhsbasic;
	@List(value="setlhsbasic", delim="comma", left="lpar", right="rpar")
	public TokenFactory setlhsbasics;
	@Choice(value={"setlhsbasics", "setlhsbasic"})
	public TokenFactory setlhs;
	@Equivalent("expr")
	public TokenFactory setrhs;
	@Sequence(value={"setlhs", "eq", "setrhs"}, required="all")
	public TokenFactory setarg_direct;
	@Sequence(value={"indirection", "eq", "setrhs"}, required="roo")
	public TokenFactory setarg_indirect;
	@Choice(value={"setarg_indirect", "setarg_direct"})
	public TokenFactory setarg;
	@List(value="setarg", delim="comma")
	public TokenFactory setargs;
	
	@Sequence(value={"colon", "deviceparams"}, required="all")
	public TokenFactory closearg_dp;
	@Sequence(value={"expr", "closearg_dp"}, required="ro")
	public TokenFactory closearg_direct;
	@Choice(value={"indirection", "closearg_direct"})
	public TokenFactory closearg;
	@List(value="closearg", delim="comma")
	public TokenFactory closeargs;
	
	@Sequence(value={"colon", "expr"}, required="all")
	public TokenFactory cexpr;
	@Sequence(value={"expr", "cexpr", "cexpr"}, required="roo")
	public TokenFactory forrhs;
	@List(value="forrhs", delim="comma")
	public TokenFactory forrhss;
	@Sequence(value={"lvn", "eq", "forrhss"}, required="all")
	public TokenFactory forarg;
	
	@CChoice(value={"gvn", "indirection"}, preds={"^", "@"}, def="lvn")
	public TokenFactory lockee_single;
	@List(value="lockee", delim="comma", left="lpar", right="rpar")
	public TokenFactory lockee_list;
	@Choice({"lockee_single", "lockee_list"})
	public TokenFactory lockee;
	@Sequence(value={"pm", "lockee", "timeout"}, required="oro")
	public TokenFactory lockarg;
	@List(value="lockarg", delim="comma")
	public TokenFactory lockargs;
	
	@List(value="lvn", delim="comma", left="lpar", right="rpar")
	public TokenFactory lvns;
	@CChoice(value={"lvns", "indirection", "intrinsic"}, preds={"(", "@", "$"}, def="name")
	public TokenFactory newarg;
	@List(value="newarg", delim="comma")
	public TokenFactory newargs;
		
	@Sequence(value={"dot", "name"}, required="all")
	public TokenFactory dname;
	@Sequence(value={"caret", "name"}, required="all")
	public TokenFactory cname;
	@Sequence(value={"name", "dname", "cname"}, required="roo")
	public TokenFactory ampersandtail;
	public TokenFactory dolamp = new TFConstString("$&");
	@Sequence(value={"dolamp", "ampersandtail", "actuallist"}, required="roo")
	public TokenFactory external;

	@Sequence(value={"comma", "expr"}, required="all")
	public TokenFactory dorderarg_1;
	@Sequence(value={"glvn", "dorderarg_1"}, required="ro")
	public TokenFactory dorderarg;
	
	@Choice({"indirection", "expr"})
	public TokenFactory xecutearg_main;
	@Sequence(value={"xecutearg_main", "postcondition"}, required="ro")
	public TokenFactory xecutearg;
	@List(value="xecutearg", delim="comma")
	public TokenFactory xecuteargs;

	@Sequence(value={"slash", "name", "actuallist"}, required="all")
	public TokenFactory writearg_slash;
	@CChoice(value={"format", "writearg_slash", "asterixexpr", "indirection"}, preds={"!#?", "/", "*", "@"}, def="expr")
	public TokenFactory writearg;
	@List(value="writearg", delim="comma")
	public TokenFactory writeargs;
	
	@List(value="name", delim="comma", left="lpar", right="rpar", none=true)
	public TokenFactory lineformal;
	
	
	@Sequence(value={"dollar", "ident"}, required="all")
	public TokenFactory intrinsicname;

	public TFCommand command = new TFCommand(this);
	public TokenFactory comment = new TFComment();
	@CChoice(value={"command", "comment"}, preds={"letter", ";"}, def="error")
	public TokenFactory commandorcomment;
	@List(value="commandorcomment", adderror=true)
	public TokenFactory commandorcommentlist;
	public TokenFactory ls = TFConstChars.getInstance(" \t");
	public TokenFactory level = TFBasic.getInstance('.', ' ');
	
	@Adapter(LineAdapter.class)
	@Sequence({"label", "lineformal", "ls", "level", "commandorcommentlist"})
	public TokenFactory line;
	
	
	
	public TFIntrinsic intrinsic = new TFIntrinsic(this);
	
	protected void initialize() {
		this.intrinsic.addVariable("D", "DEVICE"); 	
		this.intrinsic.addVariable("EC", "ECODE"); 	
		this.intrinsic.addVariable("ES", "ESTACK"); 	
		this.intrinsic.addVariable("ET", "ETRAP"); 	
		this.intrinsic.addVariable("H", "HOROLOG"); 	
		this.intrinsic.addVariable("I", "IO"); 	
		this.intrinsic.addVariable("J", "JOB"); 	
		this.intrinsic.addVariable("K", "KEY"); 	
		this.intrinsic.addVariable("PD", "PDISPLAY"); 	
		this.intrinsic.addVariable("P", "PRINCIPAL"); 	
		this.intrinsic.addVariable("Q", "QUIT"); 	
		this.intrinsic.addVariable("S", "STORAGE"); 	
		this.intrinsic.addVariable("ST", "STACK"); 	
		this.intrinsic.addVariable("SY", "SYSTEM"); 	
		this.intrinsic.addVariable("T", "TEST"); 	
		this.intrinsic.addVariable("X", "X"); 	
		this.intrinsic.addVariable("Y", "Y"); 	

		this.intrinsic.addFunction(this.exprlist, "A", "ASCII", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "C", "CHAR", 1, 999); 	
		this.intrinsic.addFunction(this.exprlist, "D", "DATA", 1, 1); 	
		this.intrinsic.addFunction(this.exprlist, "E", "EXTRACT", 1, 3); 	
		this.intrinsic.addFunction(this.exprlist, "F", "FIND", 2, 3); 	
		this.intrinsic.addFunction(this.exprlist, "G", "GET", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "I", "INCREMENT", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "J", "JUSTIFY", 2, 3); 	
		this.intrinsic.addFunction(this.exprlist, "L", "LENGTH", 1, 2); 		
		this.intrinsic.addFunction(this.dorderarg, "O", "ORDER", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "P", "PIECE", 2, 4); 	
		this.intrinsic.addFunction(this.exprlist, "Q", "QUERY", 1, 1); 	
		this.intrinsic.addFunction(this.exprlist, "R", "RANDOM", 1, 1); 	
		this.intrinsic.addFunction(this.exprlist, "RE", "REVERSE", 1, 1);		
		this.intrinsic.addFunction(this.dselectarg, "S", "SELECT", 1, 999);
		this.intrinsic.addFunction(this.cmdgargmain, "T", "TEXT", 1, 1); 
		this.intrinsic.addFunction(this.exprlist, "V", "VIEW", 1, 999); 	
		this.intrinsic.addFunction(this.exprlist, "FN", "FNUMBER", 2, 3); 	
		this.intrinsic.addFunction(this.exprlist, "N", "NEXT", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "NA", "NAME", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "Q", "QUERY", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "QL", "QLENGTH", 1, 2); 	
		this.intrinsic.addFunction(this.exprlist, "QS", "QSUBSCRIPT", 1, 3);
		this.intrinsic.addFunction(this.exprlist, "ST", "STACK", 1, 2);
		this.intrinsic.addFunction(this.exprlist, "TR", "TRANSLATE", 1, 3);
		this.intrinsic.addFunction(this.exprlist, "WFONT", 4, 4);
		this.intrinsic.addFunction(this.exprlist, "WTFIT", 6, 6);
		this.intrinsic.addFunction(this.exprlist, "WTWIDTH", 5, 5);

		this.intrinsic.addVariable("ZA");
		this.intrinsic.addVariable("ZB");
		this.intrinsic.addVariable("ZC");
		this.intrinsic.addVariable("ZE");
		this.intrinsic.addVariable("ZH");
		this.intrinsic.addVariable("ZJ");
		this.intrinsic.addVariable("ZJOB");	
		this.intrinsic.addVariable("ZR");
		this.intrinsic.addVariable("ZT");
		this.intrinsic.addVariable("ZV");
		this.intrinsic.addVariable("ZIO");	
		this.intrinsic.addVariable("ZIOS");	
		this.intrinsic.addVariable("ZVER");
		this.intrinsic.addVariable("ZEOF");
		this.intrinsic.addVariable("ZNSPACE");
		this.intrinsic.addVariable("ZINTERRUPT");
		this.intrinsic.addVariable("ZRO");
		this.intrinsic.addVariable("R");
		this.intrinsic.addVariable("ZS");
		this.intrinsic.addVariable("ZROUTINES");
		this.intrinsic.addVariable("ETRAP");
		this.intrinsic.addVariable("ZTIMESTAMP");
		this.intrinsic.addVariable("ZERROR");
		this.intrinsic.addVariable("ZCMDLINE");
		this.intrinsic.addVariable("ZPOSITION");
		this.intrinsic.addFunction(this.exprlist, "ZBITGET");
		this.intrinsic.addFunction(this.exprlist, "ZBN");
		this.intrinsic.addFunction(this.exprlist, "ZC");
		this.intrinsic.addFunction(this.exprlist, "ZF");
		this.intrinsic.addFunction(this.exprlist, "ZJ");
		this.intrinsic.addFunction(this.exprlist, "ZU");
		this.intrinsic.addFunction(this.exprlist, "ZUTIL");
		this.intrinsic.addFunction(this.exprlist, "ZTRNLNM");	
		this.intrinsic.addFunction(this.exprlist, "ZBOOLEAN");	
		this.intrinsic.addFunction(this.exprlist, "ZDEV");	
		this.intrinsic.addFunction(this.exprlist, "ZGETDV");
		this.intrinsic.addFunction(this.exprlist, "ZSORT");
		this.intrinsic.addFunction(this.exprlist, "ZESCAPE");
		this.intrinsic.addFunction(this.exprlist, "ZSEARCH");
		this.intrinsic.addFunction(this.exprlist, "ZPARSE");
		this.intrinsic.addFunction(this.exprlist, "ZCONVERT");
		this.intrinsic.addFunction(this.exprlist, "ZDVI");
		this.intrinsic.addFunction(this.exprlist, "ZGETDVI");
		this.intrinsic.addFunction(this.exprlist, "ZOS");
		this.intrinsic.addFunction(this.exprlist, "ZINTERRUPT");
		this.intrinsic.addFunction(this.exprlist, "ZJOB");
		this.intrinsic.addFunction(this.exprlist, "ZBITSTR");
		this.intrinsic.addFunction(this.exprlist, "ZBITXOR");
		this.intrinsic.addFunction(this.exprlist, "LISTGET");
		this.intrinsic.addFunction(this.exprlist, "ZDEVSPEED");
		this.intrinsic.addFunction(this.exprlist, "ZGETJPI");
		this.intrinsic.addFunction(this.exprlist, "ZGETSYI");
		this.intrinsic.addFunction(this.exprlist, "ZUTIL");	
		this.intrinsic.addFunction(this.exprlist, "ZK");	
		this.intrinsic.addFunction(this.exprlist, "ZWA");
		this.intrinsic.addFunction(this.exprlist, "ZVERSION");

		this.command.addCommands(this);

		this.command.addCommand("ZB", this);
		this.command.addCommand("ZS", this);
		this.command.addCommand("ZC", this);
		this.command.addCommand("ZR", this);
		this.command.addCommand("ZI", this);
		this.command.addCommand("ZQ", this);
		this.command.addCommand("ZT", this);
		this.command.addCommand("ZU", this);
		this.command.addCommand("ZSHOW", this);
		this.command.addCommand("ZNSPACE", this);
		this.command.addCommand("ZETRAP", this);
		this.command.addCommand("ESTART", this);
		this.command.addCommand("ESTOP", this);
		this.command.addCommand("ABORT", this);
		this.command.addCommand("ZRELPAGE", this);
		this.command.addCommand("ZSYSTEM", this);
		this.command.addCommand("ZLINK", this);		
		this.command.addCommand("ZESCAPE", this);
		this.command.addCommand("ZITRAP", this);
		this.command.addCommand("ZGETPAGE", this);
		
		String[] ops = {
				"-", "+", "_", "*", "/", "#", "\\", "**", 
				"&", "!", "=", "<", ">", "[", "]", "?", "]]",
				"'&", "'!", "'=", "'<", "'>", "'[", "']", "'?", "']]"};
		for (String op : ops) {
			this.operator.addOperator(op);
		}
	}
	
	
	public static class CacheSupply extends MTFSupply {
		public static class ObjTailAdapter implements SequenceAdapter {
			@Override
			public Token convert(Token[] tokens) {					
				return new TObjectTail(tokens);
			}
		}
		public static class LvnAdapter implements SequenceAdapter {
			@Override
			public Token convert(Token[] tokens) {
				if ((tokens[1] != null) && (tokens[1] instanceof TObjectTail)) {					
					return new TObjectExpr(tokens);
				} else {					
					return new TLocal(tokens);
				}
			}		
		}
					
		@Choice({"glvn", "expritem", "classmethod"})
		public TokenFactory expratom;
		
		@Sequence(value={"dot", "name"}, required="all")
		public TokenFactory lvn_objtail_ms;
		@List("lvn_objtail_ms")
		public TokenFactory lvn_objtail_m;
		
		@Adapter(ObjTailAdapter.class)
		@Sequence(value={"lvn_objtail_m", "actuallist"}, required="ro")
		public TokenFactory lvn_objtail;
		
		@Choice(value={"exprlistinparan", "lvn_objtail"})
		public TokenFactory lvn_next;
		
		@Adapter(LvnAdapter.class)
		@Sequence(value={"name", "lvn_next"}, required="ro")
		public TokenFactory lvn;
		
		@Choice({"expratom", "classmethod"})
		public TokenFactory expr_0;
		@Sequence(value={"expr_0", "exprtail"}, required="ro")
		public TokenFactory expr;
		
		public TokenFactory ppclass = new TFConstString("##class");
		@Sequence(value={"dot", "name"}, required="all")
		public TokenFactory classreftail;
		@List("classreftail")
		public TokenFactory classreftaillst;
		@Sequence(value={"name", "classreftaillst"}, required="ro")
		public TokenFactory classref;
		@Sequence(value={"ppclass", "lpar", "classref", "rpar", "dot", "name", "actuallist"}, required="all")
		public TokenFactory classmethod;
		
		public TokenFactory system = new TFConstString("$SYSTEM", true);
		@Sequence(value={"dot", "name"}, required="all")
		public TokenFactory method;
		@List(value="method")
		public TokenFactory methods;
		@Sequence(value={"system", "methods", "actuallist"}, required="ror")
		public TokenFactory systemcall;
		
		@Sequence(value={"label", "method"}, required="ro")
		public TokenFactory labelpiece_0;
		@CChoice(value={"indirection", "classmethod", "systemcall"}, preds={"@", "#", "$"}, def="labelpiece_0")
		public TokenFactory labelpiece;
	
		@Choice({"methods", "lineoffset"})
		public TokenFactory dlabelwoffset_1;
		@Sequence(value={"label", "dlabelwoffset_1"}, required="ro")
		public TokenFactory dlabelwoffset;		
		@Choice({"indirection", "systemcall", "classmethod", "dlabelwoffset"})
		public TokenFactory dentryspec_0;
	
		@Sequence(value={"environment", "name", "method"}, required="oro")
		public TokenFactory doroutine;

		@Choice({"classmethod", "expr"})
		public TokenFactory setrhs;
		
		public TokenFactory empty = TFEmpty.getInstance();
		@CChoice(value={"empty"}, preds={":"}, def="expr")
		public TokenFactory casearg_0;
		@Sequence(value={"casearg_0", "colon", "expr"}, required="all")
		public TokenFactory casearg_1;		
		@List(value="casearg_1", delim="comma")
		public TokenFactory casearg_list;			
		@Sequence(value={"comma", "casearg_list"}, required="all")
		public TokenFactory cases;
		@Sequence(value={"expr", "cases"}, required="all")
		public TokenFactory dcasearg;
	
		@List(value="actual", delim="comma")
		public TokenFactory dsystemarg;
		
		@Sequence(value={"dollar", "ident", "methods"}, required="rro")
		public TokenFactory intrinsicname;

		@Override
		protected void initialize() {		
			super.initialize();
			this.intrinsic.addFunction(this.dcasearg, "CASE", 1, Integer.MAX_VALUE);			
			this.intrinsic.addFunction(this.dsystemarg, "SYS", "SYSTEM", 1, Integer.MAX_VALUE);
			
			this.operator.addOperator(">=");
			this.operator.addOperator("<=");
			this.operator.addOperator("&&");
			this.operator.addOperator("||");
		}
	}
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
		
	public static MTFSupply getInstance(MVersion version) {
		try {
			Parser parser = new Parser();
			switch (version) {
				case CACHE: {
					if (CACHE_SUPPLY == null) {
						CACHE_SUPPLY = parser.parse(CacheSupply.class);
						CACHE_SUPPLY.initialize();
					}
					return CACHE_SUPPLY;
				}
				case ANSI_STD_95: {
					if (STD_95_SUPPLY == null) {
						STD_95_SUPPLY = parser.parse(MTFSupply.class);
						STD_95_SUPPLY.initialize();
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
