package com.raygroupintl.m.token;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.TFSyntaxError;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.CharSpecified;
import com.raygroupintl.parser.annotation.Choice;
import com.raygroupintl.parser.annotation.Rule;
import com.raygroupintl.parser.annotation.List;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.parser.annotation.Parser;
import com.raygroupintl.parser.annotation.Sequence;
import com.raygroupintl.parser.annotation.TokenType;
import com.raygroupintl.parser.annotation.WordSpecified;

public class MTFSupply {
	@CharSpecified(chars={'.'}, single=true)
	public TokenFactory dot;
	@CharSpecified(chars={','}, single=true)
	public TokenFactory comma;

	@CharSpecified(chars={'('}, single=true)
	public TokenFactory lpar;
	@CharSpecified(chars={')'}, single=true)
	public TokenFactory rpar;
	@CharSpecified(chars={'='}, single=true)
	public TokenFactory eq;
	@CharSpecified(chars={':'}, single=true)
	public TokenFactory colon;
	@Rule("' '")
	public TokenFactory space;
	
	@Rule("{'a'...'x' + 'A'...'X'}")
	public TokenFactory paton;
	@Rule("'y' + 'Y', {'a'...'z' + 'A'...'Z' - 'y' - 'Y'}, 'y' + 'Y'")
	public TokenFactory patony;
	@Rule("'z' + 'Z', {'a'...'y' + 'A'...'Y'}, 'z' + 'Z'")
	public TokenFactory patonz;
	@Choice({"paton", "patony", "patonz"})
	public TokenFactory patons;
	@Rule("['\\''], patons")
	public TokenFactory patcode;	
	@Rule("[intlit], ['.'], [intlit]")
	public TokenFactory repcount;
	@Rule("alternation | patcode | strlit")
	public TokenFactory patatom_re;
	@Sequence(value={"repcount", "patatom_re"}, required="all")
	public TokenFactory patatom;	
	@List(value="patatoms", delim="comma", left="lpar", right="rpar")	
	public TokenFactory alternation;	
	@List(value="patatom")	
	public TokenFactory patatoms;
	@Choice({"indirection", "patatoms"})
	public TokenFactory pattern;
	
	@Rule("'%' + 'a'...'z' + 'A'...'Z', [{'a'...'z' + 'A'...'Z' + '0'...'9'}]")
	public TokenFactory name;	
	
	@Rule("{'a'...'z' + 'A'...'Z'}")
	public TokenFactory ident;
	
	@TokenType(TIntLit.class)
	@Rule("{'0'...'9'}")
	public TokenFactory intlit;
	
	@TokenType(TLabel.class)
	@Rule("name | intlit")
	public TokenFactory label;
	
	@Rule("'^', [environment], name")
	public TokenFactory envroutine;
	
	@TokenType(TNumLit.class)
	@Rule("'.', intlit, ['E', ['+' | '-'], intlit]")
	public TokenFactory numlita;
	@TokenType(TNumLit.class)
	@Rule("intlit, ['.', intlit], ['E', ['+' | '-'], intlit]")
	public TokenFactory numlitb;
	
	@Rule("numlita | numlitb")
	public TokenFactory numlit;
	
	public TFOperator operator = new TFOperator("operator");
	public TokenFactory error = new TFSyntaxError("error", MError.ERR_GENERAL_SYNTAX);
	
	@Rule("'+' + '-' + '\\''")
	public TokenFactory unaryop;
	
	@TokenType(TStringLiteral.class)
	@Rule("('\"', [{-'\\r' - '\\n' - '\"'}], '\"'), [strlit]")	
	public TokenFactory strlit;
	
	@TokenType(TEnvironment.class)
	@Rule("('|', expr, '|') | {expratom:',':'[':']'}")
	public TokenFactory environment;
	
	@Rule("'?', pattern")
	public TokenFactory exprtaila;
	@Rule("\"'?\", pattern")
	public TokenFactory exprtailb;
	@Rule("operator, expratom")
	public TokenFactory exprtailc;
	@Rule("exprtaila | exprtailb | exprtailc")
	public TokenFactory exprtails;
	@List("exprtails")
	public TokenFactory exprtail;

	@Rule("{expr:','}")
	public TokenFactory exprlist;
	
	@Rule("{expr:',':'(':')'}")
	public TokenFactory exprlistinparan;

	@Rule("'(', expr, ')'")
	public TokenFactory exprinpar;
	
	@Rule("'=', expr")
	public TokenFactory eqexpr;
	
	@TokenType(TIndirection.class)
	@Rule("'@', expratom, [\"@(\", exprlist, ')']")
	public TokenFactory indirection;
	@TokenType(TIndirection.class)
	@Rule("'@', expratom")
	public TokenFactory rindirection;
		
	@Rule("lvn | gvnall | indirection")
	public TokenFactory glvn;
	
	@TokenType(TGlobalNamed.class)
	@Rule("'^', ([environment], name, [exprlistinparan])")
	public TokenFactory gvn;
	
	@Rule("\"^$\", ident, exprlistinparan")
	public TokenFactory gvnssvn;

	@Rule("\"$$\", extrinsicarg")
	public TokenFactory extrinsic;
	
	@Rule("unaryop, expratom")
	public TokenFactory unaryexpritem;
	
	@TokenType(TGlobalNaked.class)
	@Rule("'^', exprlistinparan")
	public TokenFactory gvnnaked;
	
	@Rule("expr, ':', expr")
	public TokenFactory dselectarge;
	@List(value="dselectarge", delim="comma")
	public TokenFactory dselectarg;
	
	@Rule("gvnssvn | gvnnaked | gvn")
	public TokenFactory gvnall;

	@Rule("extrinsic | external | intrinsic")
	public TokenFactory expritemd;
	
	@Rule("strlit | expritemd | unaryexpritem | numlit | exprinpar")
	public TokenFactory expritem;
	
	@Rule("'.', name")
	public TokenFactory actualda;
	@Rule("'.', indirection")
	public TokenFactory actualdb;
	@Rule("numlita | actualda | actualdb | expr")
	public TokenFactory actual;
	
	@Choice({"glvn", "expritem"})
	public TokenFactory expratom;

	@TokenType(TLocal.class)
	@Sequence(value={"name", "exprlistinparan"}, required="ro")
	public TokenFactory lvn;
	
	@Rule("expratom, [exprtail]")
	public TokenFactory expr;
	
	@TokenType(TActualList.class)
	@List(value="actual", delim="comma", left="lpar", right="rpar", empty=true, none=true)
	public TokenFactory actuallist;

	@Rule("'=', expr")
	public TokenFactory deviceparama;
	@Rule("expr, [deviceparama]")
	public TokenFactory deviceparam;
	@List(value="deviceparam", delim="colon", left="lpar", right="rpar", empty=true)
	public TokenFactory deviceparamsi;
	@Rule("deviceparamsi | deviceparam")
	public TokenFactory deviceparams;
	
	@Choice({"indirection", "name"})
	public TokenFactory cmdkexcarg;
	@List(value="cmdkexcarg", delim="comma", left="lpar", right="rpar")
	public TokenFactory cmdkexcargs;
	@Rule("cmdkexcargs | indirection | glvn")
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
	
	@Rule("exprlistinparan | expr")
	public TokenFactory exprorinlist;
	@Rule("colon, [deviceparams], [colon], [expr], [colon], [exprorinlist]")
	public TokenFactory cmdoargtail;
	@Rule("expr, [cmdoargtail]")
	public TokenFactory cmdoargbasic;
	@Choice({"indirection", "cmdoargbasic"})
	public TokenFactory cmdoarg;
	@List(value="cmdoarg", delim="comma")
	public TokenFactory cmdoargs;
		
	@Choice({"indirection", "label"})
	public TokenFactory linetagname;
	@Rule("'+', expr")
	public TokenFactory lineoffset;
	@Sequence(value={"linetagname", "lineoffset"})
	public TokenFactory tagspec;
	@Sequence(value={"environment", "name"}, required="or")
	public TokenFactory envname;
	@Choice(value={"rindirection", "envname"})
	public TokenFactory routinespeccc;
	@Rule("'^', routinespeccc")
	public TokenFactory routinespec;
	@Sequence({"tagspec", "routinespec"})
	public TokenFactory cmdgargmain;
	@Sequence(value={"cmdgargmain", "postcondition"}, required="ro")
	public TokenFactory gotoargument;
	@List(value="gotoargument", delim="comma")
	public TokenFactory gotoarguments;
	
	@Rule("'#', expr")
	public TokenFactory readcount;

	@Rule("'?', expr")
	public TokenFactory tabformat;
	@Rule("{'!' + '#'}, [tabformat]")
	public TokenFactory xtabformat;
	@Rule("tabformat | xtabformat")
	public TokenFactory format;
	
	@Rule("glvn, [readcount], [timeout]")
	public TokenFactory cmdrargdef;	
	@Rule("'*', [glvn], [timeout]")
	public TokenFactory cmdrargast;	
	@Rule("indirection, [timeout]")
	public TokenFactory cmdrargat;	
	@Rule("format | strlit | cmdrargast | cmdrargat | cmdrargdef")
	public TokenFactory cmdrarg;
	@List(value="cmdrarg", delim="comma")
	public TokenFactory cmdrargs;
		
	@Rule("':', expr")
	public TokenFactory postcondition;
	@Rule("'*', expr")
	public TokenFactory asterixexpr;
	@Rule("':', expr")
	public TokenFactory timeout;
	
	@List(value="expr", delim="colon", left="lpar", right="rpar", empty=true)
	public TokenFactory usedeviceparamlist;
	@Rule("usedeviceparamlist | expr")
	public TokenFactory usedeviceparam;
	@Sequence(value={"colon", "usedeviceparam"}, required="ro")
	public TokenFactory colonusedeviceparam;
	@Rule("expr, [colonusedeviceparam], [colonusedeviceparam]")
	public TokenFactory cmduarg;
	@List(value="cmduarg", delim="comma")
	public TokenFactory cmduargs;
	
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
	@Sequence(value={"labelpiece", "lineoffset", "doroutinef", "actuallist"}, required="oooo")
	public TokenFactory extrinsicarg;
	
	@TokenType(TExtDoArgument.class)
	@Rule("'&', name, ['.', name], ['^', name], [actuallist], [postcondition]")
	public TokenFactory extdoargument;

	@TokenType(TDoArgument.class)	
	@Rule("rindirection, [dolineoffset], [doroutinef], [postcondition]")
	public TokenFactory inddoargument;

	@TokenType(TDoArgument.class)	
	@Rule("label, dolineoffset, [doroutinef], [postcondition]")
	public TokenFactory offsetdoargument;

	@TokenType(TDoArgument.class)	
	@Rule("label, actuallist, [postcondition]")
	public TokenFactory labelcalldoargument;

	@TokenType(TDoArgument.class)	
	@Rule("label, ['^', ((envdoroutine | doroutine) , [actuallist]) | inddoroutine], [postcondition]")
	public TokenFactory doargument;
	
	@TokenType(TDoArgument.class)	
	@Rule("'^', noindroutinepostcaret, [actuallist], [postcondition]")
	public TokenFactory onlyrsimpledoargument;
	
	@TokenType(TDoArgument.class)	
	@Rule("'^', indirection, [postcondition]")
	public TokenFactory onlyrdoargument;
	
	@Rule("extdoargument | inddoargument | offsetdoargument | labelcalldoargument | doargument | onlyrsimpledoargument | onlyrdoargument")
	public TokenFactory doargumentall;
	
	@List(value="doargumentall", delim="comma")
	public TokenFactory doarguments;
	
	@TokenType(BasicTokens.MTEnvironmentFanoutRoutine.class)
	@Rule("environment, name")
	public TokenFactory envdoroutine;
	
	@TokenType(BasicTokens.MTFanoutRoutine.class)
	@Rule("name")
	public TokenFactory doroutine;
	
	@Rule("envdoroutine | doroutine")
	public TokenFactory noindroutinepostcaret;
	
	@TokenType(BasicTokens.MTIndirectFanoutRoutine.class)
	@Rule("rindirection")
	public TokenFactory inddoroutine;
	
	@Rule("envdoroutine | doroutine | inddoroutine")
	public TokenFactory doroutinepostcaret;
	
	@Rule("'^', doroutinepostcaret")
	public TokenFactory doroutinef;
	
	@Rule("'+', expr")
	public TokenFactory dolineoffset;

	@Choice(value={"indirection", "label"})
	public TokenFactory labelpiece;
	
	@Choice(value={"indirection", "intrinsic", "glvn"})
	public TokenFactory setlhsbasic;
	@List(value="setlhsbasic", delim="comma", left="lpar", right="rpar")
	public TokenFactory setlhsbasics;
	@Choice(value={"setlhsbasics", "setlhsbasic"})
	public TokenFactory setlhs;
	
	@Rule("expr")
	public TokenFactory setrhs;
	
	@Sequence(value={"setlhs", "eq", "setrhs"}, required="all")
	public TokenFactory setarg_direct;
	@Sequence(value={"indirection", "eq", "setrhs"}, required="roo")
	public TokenFactory setarg_indirect;
	@Choice(value={"setarg_indirect", "setarg_direct"})
	public TokenFactory setarg;
	@List(value="setarg", delim="comma")
	public TokenFactory setargs;
	
	@Rule("colon, deviceparams")
	public TokenFactory closeargdp;
	@Rule("expr, [closeargdp]")
	public TokenFactory closeargdirect;
	@Rule("indirection | closeargdirect")
	public TokenFactory closearg;
	@Rule("{closearg:','}")
	public TokenFactory closeargs;
	
	@Rule("':', expr")
	public TokenFactory cexpr;
	@Rule("expr, [cexpr], [cexpr]")
	public TokenFactory forrhs;
	@Rule("{forrhs:','}")
	public TokenFactory forrhss;
	@Rule("lvn, '=', forrhss")
	public TokenFactory forarg;
	
	@Rule("gvn | indirection | lvn")
	public TokenFactory lockeesingle;
	@Rule("lockeesingle | {lockee:',':'(':')'}")
	public TokenFactory lockee;
	@Rule("['+' + '-'], lockee, [timeout]")
	public TokenFactory lockarg;
	@Rule("{lockarg:','}")
	public TokenFactory lockargs;
	
	@Rule("{lvn:',':'(':')'} | indirection | intrinsic | name")
	public TokenFactory newarg;
	@Rule("{newarg:','}")
	public TokenFactory newargs;
		
	@Rule("'.', name")
	public TokenFactory dname;
	@Rule("'^', name")
	public TokenFactory cname;
	@Sequence(value={"name", "dname", "cname"}, required="roo")
	public TokenFactory ampersandtail;
	@WordSpecified("$&")
	public TokenFactory dolamp;
	@Sequence(value={"dolamp", "ampersandtail", "actuallist"}, required="roo")
	public TokenFactory external;

	@Rule("',', expr")
	public TokenFactory dorderarga;
	@Rule("glvn, [dorderarga]")
	public TokenFactory dorderarg;
	
	@Rule("indirection | expr")
	public TokenFactory xecutearg_main;
	@Sequence(value={"xecutearg_main", "postcondition"}, required="ro")
	public TokenFactory xecutearg;
	@List(value="xecutearg", delim="comma")
	public TokenFactory xecuteargs;

	@Rule("'/', name, actuallist")
	public TokenFactory writeargslash;
	@Rule("format | writeargslash | asterixexpr | indirection | expr")
	public TokenFactory writearg;
	@List(value="writearg", delim="comma")
	public TokenFactory writeargs;
	
	@List(value="name", delim="comma", left="lpar", right="rpar", none=true)
	public TokenFactory lineformal;
		
	@Rule("'$', ident")
	public TokenFactory intrinsicname;

	@Rule("{' '} | comment | end")
	public TokenFactory commandend;
	
	public TFCommand command = new TFCommand("command", this);
	
	@TokenType(TComment.class)
	@Rule("';', [{- '\\r' - '\\n'}]")
	public TokenFactory comment;
	
	@Rule("{command | comment | error}")
	public TokenFactory commandorcommentlist;
	
	@Rule("{' ' + '\\t'}")
	public TokenFactory ls;
	
	@Rule("{' ' + '.'}")
	public TokenFactory level;
	
	@TokenType(TLine.class)
	@Rule("[label], [lineformal], [ls], [level], [commandorcommentlist]")
	public TokenFactory line;
	
	public TFIntrinsic intrinsic = new TFIntrinsic("intrinsic", this);
	
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
		this.intrinsic.addVariable("EREF");
		this.intrinsic.addVariable("ZDIR");
		this.intrinsic.addVariable("ZS");
		this.intrinsic.addVariable("ZROUTINES");
		this.intrinsic.addVariable("ZGBLDIR");
		this.intrinsic.addVariable("ZN");
		this.intrinsic.addVariable("ZSTATUS");
		this.intrinsic.addVariable("REFERENCE");
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
		@Choice({"glvn", "expritem", "classmethod"})
		public TokenFactory expratom;
		
		@TokenType(TObjectExpr.class)
		@Rule("name, '.', {name:'.'}, [actuallist]")
		public TokenFactory objectexpr;
		
		@Rule("objectexpr | lvn | gvnall | indirection")
		public TokenFactory glvn;
			
		@Rule("expratom | classmethod, [exprtail]")
		public TokenFactory expr;
		
		@WordSpecified("##class")
		public TokenFactory ppclass;
		@Sequence(value={"dot", "name"}, required="all")
		public TokenFactory classreftail;
		@Rule("{classreftail}")
		public TokenFactory classreftaillst;
		@Rule("name, [classreftaillst]")
		public TokenFactory classref;
		@Sequence(value={"ppclass", "lpar", "classref", "rpar", "dot", "name", "actuallist"}, required="all")
		public TokenFactory classmethod;
		
		@WordSpecified(value="$SYSTEM", ignorecase=true)
		public TokenFactory system;
		@Rule("'.', name")
		public TokenFactory method;
		@List(value="method")
		public TokenFactory methods;
		@Sequence(value={"system", "methods", "actuallist"}, required="ror")
		public TokenFactory systemcall;
		
		@Rule("label, method, [dolineoffset], [doroutinef], [actuallist], [postcondition]")
		public TokenFactory objdoargument;
		@Rule("classmethod, [dolineoffset], [doroutinef], [actuallist], [postcondition]")
		public TokenFactory clsdoargument;
		@Rule("systemcall, [dolineoffset], [doroutinef], [actuallist], [postcondition]")
		public TokenFactory sysdoargument;
		@TokenType(TDoArgument.class)	
		@Rule("label, ['^', ((envdoroutine | objdoroutine | doroutine) , [actuallist]) | inddoroutine], [postcondition]")
		public TokenFactory doargument;

		@Rule("objdoargument | extdoargument | inddoargument | offsetdoargument | labelcalldoargument | doargument | onlyrsimpledoargument | onlyrdoargument | clsdoargument | sysdoargument")
		public TokenFactory doargumentall;
		
		@Rule("name, method")
		public TokenFactory objdoroutine;
		@Rule("envdoroutine | objdoroutine | doroutine | inddoroutine")
		public TokenFactory doroutinepostcaret;
		
		@Rule("classmethod | expr")
		public TokenFactory setrhs;
		
		@Rule("expr, ',', {([expr], [':', expr]):','}")
		public TokenFactory dcasearg;
	
		@List(value="actual", delim="comma")
		public TokenFactory dsystemarg;
		
		@Rule("'$', ident, [methods]")
		public TokenFactory intrinsicname;

		@Override
		protected void initialize() {		
			super.initialize();
			this.intrinsic.addFunction(this.dcasearg, "CASE", 1, Integer.MAX_VALUE);			
			this.intrinsic.addFunction(this.dsystemarg, "SYS", "SYSTEM", 0, Integer.MAX_VALUE);
			
			this.operator.addOperator(">=");
			this.operator.addOperator("<=");
			this.operator.addOperator("&&");
			this.operator.addOperator("||");
		}
	}
	
	private static MTFSupply CACHE_SUPPLY;
	private static MTFSupply STD_95_SUPPLY;
	
	private static MTFSupply generateSupply(Class<? extends MTFSupply> cls) throws ParseException {
		Parser parser = new Parser();
		MTFSupply result = 	parser.parse(cls);
		result.initialize();
		return result;
	}
	
	public static MTFSupply getInstance(MVersion version) throws ParseException {
		switch (version) {
			case CACHE: {
				if (CACHE_SUPPLY == null) {
					CACHE_SUPPLY = generateSupply(CacheSupply.class);
				}
				return CACHE_SUPPLY;
			}
			case ANSI_STD_95: {
				if (STD_95_SUPPLY == null) {
					STD_95_SUPPLY = generateSupply(MTFSupply.class);
				}
				return STD_95_SUPPLY;
			}
			default:
				throw new IllegalArgumentException("Unknown M version");
		}
	}	
}
