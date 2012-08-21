package com.raygroupintl.m.token;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.TFSyntaxError;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.Rule;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.parser.annotation.Parser;
import com.raygroupintl.parser.annotation.TokenType;

public class MTFSupply {
	@Rule("'('")
	public TokenFactory lpar;
	@Rule("')'")
	public TokenFactory rpar;
	@Rule("' '")
	public TokenFactory space;
	
	@Rule("{'a'...'x' + 'A'...'X'}")
	public TokenFactory paton;
	@Rule("'y' + 'Y', {'a'...'z' + 'A'...'Z' - 'y' - 'Y'}, 'y' + 'Y'")
	public TokenFactory patony;
	@Rule("'z' + 'Z', {'a'...'y' + 'A'...'Y'}, 'z' + 'Z'")
	public TokenFactory patonz;
	@Rule("paton | patony | patonz")
	public TokenFactory patons;
	@Rule("['\\''], patons")
	public TokenFactory patcode;	
	@Rule("[intlit], ['.'], [intlit]")
	public TokenFactory repcount;
	@Rule("alternation | patcode | strlit")
	public TokenFactory patatomre;
	@Rule("repcount, patatomre")
	public TokenFactory patatom;	
	@Rule("{patatoms:',':'(':')'}")
	public TokenFactory alternation;	
	@TokenType(StringTokens.PatAtoms.class)	
	@Rule("{patatom}")	
	public TokenFactory patatoms;
	@Rule("indirection | patatoms")
	public TokenFactory pattern;
	
	@TokenType(StringTokens.MName.class)
	@Rule("'%' + 'a'...'z' + 'A'...'Z', [{'a'...'z' + 'A'...'Z' + '0'...'9'}]")
	public TokenFactory name;	
	
	@TokenType(StringTokens.MIdent.class)
	@Rule("{'a'...'z' + 'A'...'Z'}")
	public TokenFactory ident;
	
	@TokenType(MIntLit.class)
	@Rule("{'0'...'9'}")
	public TokenFactory intlit;
	
	@Rule("name | intlit")
	public TokenFactory label;
	
	@Rule("'^', [environment], name")
	public TokenFactory envroutine;
	
	@TokenType(MNumLit.class)
	@Rule("'.', intlit, ['E', ['+' | '-'], intlit]")
	public TokenFactory numlita;
	@TokenType(MNumLit.class)
	@Rule("intlit, ['.', intlit], ['E', ['+' | '-'], intlit]")
	public TokenFactory numlitb;
	
	@Rule("numlita | numlitb")
	public TokenFactory numlit;
	
	public TFOperator operator = new TFOperator("operator");
	public TokenFactory error = new TFSyntaxError("error", MError.ERR_GENERAL_SYNTAX);
	
	@Rule("'+' + '-' + '\\''")
	public TokenFactory unaryop;
	
	@TokenType(MStringLiteral.class)
	@Rule("('\"', [{-'\\r' - '\\n' - '\"'}], '\"'), [strlit]")	
	public TokenFactory strlit;
	
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
	@Rule("{exprtails}")
	public TokenFactory exprtail;

	@Rule("{expr:','}")
	public TokenFactory exprlist;
	
	@Rule("{expr:',':'(':')'}")
	public TokenFactory exprlistinparan;

	@Rule("'(', expr, ')'")
	public TokenFactory exprinpar;
	
	@TokenType(MIndirection.class)
	@Rule("'@', expratom, [\"@(\", exprlist, ')']")
	public TokenFactory indirection;
	@TokenType(MIndirection.class)
	@Rule("'@', expratom")
	public TokenFactory rindirection;
		
	@Rule("lvn | gvnall | indirection")
	public TokenFactory glvn;
	
	@TokenType(MGlobal.class)
	@Rule("'^', ([environment], name, [exprlistinparan])")
	public TokenFactory gvn;
	
	@TokenType(MSsvn.class)
	@Rule("\"^$\", ident, exprlistinparan")
	public TokenFactory gvnssvn;

	@Rule("\"$$\", extrinsicarg")
	public TokenFactory extrinsic;
	
	@Rule("unaryop, expratom")
	public TokenFactory unaryexpritem;
	
	@TokenType(MNakedGlobal.class)
	@Rule("'^', exprlistinparan")
	public TokenFactory gvnnaked;
	
	@Rule("expr, ':', expr")
	public TokenFactory dselectarge;
	@Rule("{dselectarge:','}")
	public TokenFactory dselectarg;
	
	@Rule("gvnssvn | gvnnaked | gvn")
	public TokenFactory gvnall;

	@Rule("extrinsic | external | intrinsic")
	public TokenFactory expritemd;
	
	@Rule("strlit | expritemd | unaryexpritem | numlit | exprinpar")
	public TokenFactory expritem;
	
	@TokenType(MLocalByRef.class)
	@Rule("'.', name")
	public TokenFactory actualda;
	@Rule("'.', indirection")
	public TokenFactory actualdb;
	@Rule("numlita | actualda | actualdb | expr")
	public TokenFactory actual;
	
	@Rule("glvn | expritem")
	public TokenFactory expratom;

	@TokenType(MLocal.class)
	@Rule("name, [exprlistinparan]")
	public TokenFactory lvn;
	
	@TokenType(MExpression.class)
	@Rule("expratom, [exprtail]")
	public TokenFactory expr;
	
	@TokenType(MActualList.class)
	@Rule("{actual:',':'(':')':1:1}")
	public TokenFactory actuallist;

	@Rule("'=', expr")
	public TokenFactory deviceparama;
	@Rule("expr, [deviceparama]")
	public TokenFactory deviceparam;
	@Rule("{deviceparam:':':'(':')':1}")
	public TokenFactory deviceparamsi;
	@Rule("deviceparamsi | deviceparam")
	public TokenFactory deviceparams;
	
	@Rule("exprlistinparan | expr")
	public TokenFactory exprorinlist;
	@TokenType(OpenCloseUseCmdTokens.MDeviceParameters.class)	
	@Rule("':', [deviceparams], [':'], [expr], [':'], [exprorinlist]")
	public TokenFactory cmdoargtail;
	@TokenType(OpenCloseUseCmdTokens.MAtomicOpenCmd.class)	
	@Rule("expr, [cmdoargtail]")
	public TokenFactory cmdoargbasic;
	@Rule("indirection | cmdoargbasic")
	public TokenFactory cmdoarg;
	@Rule("{cmdoarg:','}")
	public TokenFactory cmdoargs;
		
	@Rule("indirection | label")
	public TokenFactory linetagname;
	@Rule("'+', expr")
	public TokenFactory lineoffset;
	@Rule("[linetagname], [lineoffset]")
	public TokenFactory tagspec;
	@Rule("[environment], name")
	public TokenFactory envname;
	@Rule("rindirection | envname")
	public TokenFactory routinespeccc;
	@Rule("'^', routinespeccc")
	public TokenFactory routinespec;
	@Rule("[tagspec], [routinespec]")
	public TokenFactory cmdgargmain;
	
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
	@Rule("{cmdrarg:','}")
	public TokenFactory cmdrargs;
	
	@TokenType(BasicTokens.MPostCondition.class)
	@Rule("':', expr")
	public TokenFactory postcondition;
	@Rule("'*', expr")
	public TokenFactory asterixexpr;
	@Rule("':', expr")
	public TokenFactory timeout;
	
	@Rule("{expr:':':'(':')':1}")
	public TokenFactory usedeviceparamlist;
	@TokenType(OpenCloseUseCmdTokens.MDeviceParameters.class)	
	@Rule("usedeviceparamlist | expr")
	public TokenFactory usedeviceparam;
	@Rule("':', [usedeviceparam]")
	public TokenFactory colonusedeviceparam;
	@Rule("expr, [colonusedeviceparam], [colonusedeviceparam]")
	@TokenType(OpenCloseUseCmdTokens.MAtomicUseCmd.class)	
	public TokenFactory cmduarg;
	@Rule("{cmduarg:','}")
	public TokenFactory cmduargs;
	
	@Rule("label, [lineoffset]")
	public TokenFactory labelwoffset;
	@Rule("rindirection | labelwoffset")
	public TokenFactory entryspeca;
	@Rule("[entryspeca], [routinespec], [actuallist], [colonusedeviceparam], [timeout]")
	public TokenFactory cmdjarg;
	@Rule("'^', [usedeviceparam]")
	public TokenFactory jobparams;
	@Rule("{cmdjarg:','}")
	public TokenFactory cmdjargs;
	
	
	
	@TokenType(MExtrinsic.class)	
	@Rule("indfanoutlabel, ['^', ((envfanoutroutine | fanoutroutine) , [actuallist]) | indfanoutroutine]")
	public TokenFactory indexargument;

	@TokenType(MExtrinsic.class)	
	@Rule("fanoutlabel, actuallist")
	public TokenFactory labelcallexargument;

	@TokenType(MExtrinsic.class)	
	@Rule("fanoutlabel, ['^', ((envfanoutroutine | fanoutroutine) , [actuallist]) | indfanoutroutine]")
	public TokenFactory exargument;
	
	@TokenType(MExtrinsic.class)	
	@Rule("'^', noindroutinepostcaret, [actuallist]")
	public TokenFactory onlyrsimpleexargument;
	
	@TokenType(MExtrinsic.class)	
	@Rule("'^', indirection")
	public TokenFactory onlyrexargument;
	
	@Rule("indexargument | labelcallexargument | exargument | onlyrsimpleexargument | onlyrexargument")
	public TokenFactory extrinsicarg;
	
	
	@TokenType(MExtDoArgument.class)
	@Rule("'&', name, ['.', name], ['^', name], [actuallist], [postcondition]")
	public TokenFactory extdoargument;

	@TokenType(BasicTokens.MTFanoutLabelA.class)
	@Rule("name")
	public TokenFactory fanoutlabela;
	@TokenType(BasicTokens.MTFanoutLabelB.class)
	@Rule("intlit")
	public TokenFactory fanoutlabelb;
	@Rule("fanoutlabela | fanoutlabelb")
	public TokenFactory fanoutlabel;
	@TokenType(BasicTokens.MTIndirectFanoutLabel.class)
	@Rule("rindirection")
	public TokenFactory indfanoutlabel;
	@TokenType(BasicTokens.MTEnvironmentFanoutRoutine.class)
	@Rule("environment, name")
	public TokenFactory envfanoutroutine;	
	@TokenType(BasicTokens.MTFanoutRoutine.class)
	@Rule("name")
	public TokenFactory fanoutroutine;
	@TokenType(BasicTokens.MTIndirectFanoutRoutine.class)
	@Rule("rindirection")
	public TokenFactory indfanoutroutine;
	@Rule("envfanoutroutine | fanoutroutine")
	public TokenFactory noindroutinepostcaret;
	
	@Rule("envfanoutroutine | fanoutroutine | indfanoutroutine")
	public TokenFactory goroutinepostcaret;
	
	@Rule("'^', goroutinepostcaret")
	public TokenFactory goroutineref;
	
	@TokenType(MGotoArgument.class)	
	@Rule("indfanoutlabel, [dolineoffset], [goroutineref], [postcondition]")
	public TokenFactory indgoargument;

	@TokenType(MGotoArgument.class)	
	@Rule("fanoutlabel, dolineoffset, [goroutineref], [postcondition]")
	public TokenFactory offsetgoargument;

	@TokenType(MGotoArgument.class)	
	@Rule("fanoutlabel, ['^', envfanoutroutine | fanoutroutine | indfanoutroutine], [postcondition]")
	public TokenFactory goargument;
	
	@TokenType(MGotoArgument.class)	
	@Rule("'^', indirection, [postcondition]")
	public TokenFactory onlyrgoargument;
	
	@TokenType(MGotoArgument.class)	
	@Rule("'^', noindroutinepostcaret, [postcondition]")
	public TokenFactory onlyrsimplegoargument;
	
	@Rule("indgoargument | offsetgoargument | goargument | onlyrsimplegoargument | onlyrgoargument")
	public TokenFactory goargumentall;
	
	@Rule("{goargumentall:','}")
	public TokenFactory gotoarguments;	
	
	@TokenType(MDoArgument.class)	
	@Rule("indfanoutlabel, [dolineoffset], [doroutineref], [postcondition]")
	public TokenFactory inddoargument;

	@TokenType(MDoArgument.class)	
	@Rule("fanoutlabel, dolineoffset, [doroutineref], [postcondition]")
	public TokenFactory offsetdoargument;

	@TokenType(MDoArgument.class)	
	@Rule("fanoutlabel, actuallist, [postcondition]")
	public TokenFactory labelcalldoargument;

	@TokenType(MDoArgument.class)	
	@Rule("fanoutlabel, ['^', ((envfanoutroutine | fanoutroutine) , [actuallist]) | indfanoutroutine], [postcondition]")
	public TokenFactory doargument;
	
	@TokenType(MDoArgument.class)	
	@Rule("'^', noindroutinepostcaret, [actuallist], [postcondition]")
	public TokenFactory onlyrsimpledoargument;
	
	@TokenType(MDoArgument.class)	
	@Rule("'^', indirection, [postcondition]")
	public TokenFactory onlyrdoargument;
	
	@Rule("extdoargument | inddoargument | offsetdoargument | labelcalldoargument | doargument | onlyrsimpledoargument | onlyrdoargument")
	public TokenFactory doargumentall;
	
	@Rule("{doargumentall:','}")
	public TokenFactory doarguments;	
	
	@Rule("envfanoutroutine | fanoutroutine | indfanoutroutine")
	public TokenFactory doroutinepostcaret;
	
	@Rule("'^', doroutinepostcaret")
	public TokenFactory doroutineref;
	
	@Rule("'+', expr")
	public TokenFactory dolineoffset;

	@Rule("intrinsic | glvn")
	public TokenFactory setlhsbasic;
	@Rule("{setlhsbasic:',':'(':')'}")
	public TokenFactory setlhsbasics;	
	@Rule("expr")
	public TokenFactory setrhs;
	@TokenType(SetCmdTokens.MSingleAtomicSetCmd.class)
	@Rule("setlhsbasic, '=', setrhs")
	public TokenFactory setargsingle;
	@TokenType(SetCmdTokens.MMultiAtomicSetCmd.class)
	@Rule("setlhsbasics, '=', setrhs")
	public TokenFactory setargmulti;
	@TokenType(SetCmdTokens.MSingleAtomicSetCmd.class)
	@Rule("indirection, ['='], [setrhs]")
	public TokenFactory setargindirect;
	@Rule("setargindirect | setargmulti | setargsingle")
	public TokenFactory setarg;
	@Rule("{setarg:','}")
	public TokenFactory setargs;
	
	@TokenType(OpenCloseUseCmdTokens.MDeviceParameters.class)	
	@Rule("':', deviceparams")
	public TokenFactory closeargdp;
	@Rule("expr, [closeargdp]")
	@TokenType(OpenCloseUseCmdTokens.MAtomicCloseCmd.class)		
	public TokenFactory closeargdirect;
	@Rule("indirection | closeargdirect")
	public TokenFactory closearg;
	@Rule("{closearg:','}")
	public TokenFactory closeargs;
	
	@Rule("expr, [':', expr], [':', expr]")
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
	
	@TokenType(NewCmdTokens.MNewedLocal.class)
	@Rule("name")
	public TokenFactory newedlocal;
	@Rule("rindirection | newedlocal")
	public TokenFactory exclusivenewsingle;
	@TokenType(NewCmdTokens.MExclusiveAtomicNewCmd.class)	
	@Rule("{exclusivenewsingle:',':'(':')'}")
	public TokenFactory exclusivenew;
	@TokenType(NewCmdTokens.MAtomicNewCmd.class)	
	@Rule("rindirection | intrinsicname | newedlocal")
	public TokenFactory normalnew;	
	@Rule("exclusivenew | normalnew")
	public TokenFactory newarg;
	@Rule("{newarg:','}")
	public TokenFactory newargs;
		
	@TokenType(KillCmdTokens.MKilledLocal.class)
	@Rule("name")
	public TokenFactory killedlocal;
	@Rule("rindirection | killedlocal")
	public TokenFactory exclusivekillsingle;
	@TokenType(KillCmdTokens.MExclusiveAtomicKillCmd.class)	
	@Rule("{exclusivekillsingle:',':'(':')'}")
	public TokenFactory exclusivekill;
	@TokenType(KillCmdTokens.MAtomicKillCmd.class)	
	@Rule("glvn")
	public TokenFactory normalkill;		
	@Rule("exclusivekill | normalkill")
	public TokenFactory killarg;
	@Rule("{killarg:','}")
	public TokenFactory killargs;

	@TokenType(MergeCmdTokens.MAtomicMergeCmd.class)
	@Rule("glvn, '=', glvn")
	public TokenFactory mergeargdirect;
	@TokenType(MergeCmdTokens.MIndirectAtomicMergeCmd.class)
	@Rule("indirection, ['=', glvn]")
	public TokenFactory mergeargindirect;
	@Rule("mergeargindirect | mergeargdirect")
	public TokenFactory mergearg;
	@Rule("{mergearg:','}")
	public TokenFactory mergeargs;
	
	@Rule("'.', name")
	public TokenFactory dname;
	@Rule("'^', name")
	public TokenFactory cname;
	@Rule("name, [dname], [cname]")
	public TokenFactory ampersandtail;
	@Rule("\"$&\", [ampersandtail], [actuallist]")
	public TokenFactory external;

	@Rule("',', expr")
	public TokenFactory dorderarga;
	@Rule("glvn, [dorderarga]")
	public TokenFactory dorderarg;
	
	@Rule("indirection | expr")
	public TokenFactory xecuteargmain;
	@Rule("xecuteargmain, [postcondition]")
	public TokenFactory xecutearg;
	@Rule("{xecutearg:','}")
	public TokenFactory xecuteargs;

	@Rule("'/', name, actuallist")
	public TokenFactory writeargslash;
	@Rule("format | writeargslash | asterixexpr | indirection | expr")
	public TokenFactory writearg;
	@Rule("{writearg:','}")
	public TokenFactory writeargs;
	
	@Rule("{name:',':'(':')':0:1}")
	public TokenFactory lineformal;
		
	@Rule("'$', ident")
	public TokenFactory intrinsicname;

	@Rule("{' '} | comment | end")
	public TokenFactory commandend;
	
	public TFCommand command = new TFCommand("command", this);
	
	@TokenType(MComment.class)
	@Rule("';', [{- '\\r' - '\\n'}]")
	public TokenFactory comment;
	
	@Rule("{command | comment | error}")
	public TokenFactory commandorcommentlist;
	
	@Rule("{' ' + '\\t'}")
	public TokenFactory ls;
	
	@Rule("{' ' + '.'}")
	public TokenFactory level;
	
	@TokenType(MLine.class)
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
		@Rule("glvn | expritem | classmethod")
		public TokenFactory expratom;
		
		@TokenType(MObjectExpr.class)
		@Rule("name, '.', {name:'.'}, [actuallist]")
		public TokenFactory objectexpr;
		
		@Rule("objectexpr | lvn | gvnall | indirection")
		public TokenFactory glvn;
			
		@TokenType(MExpression.class)
		@Rule("expratom | classmethod, [exprtail]")
		public TokenFactory expr;
		
		@Rule("\"##class\"")
		public TokenFactory ppclass;
		@Rule("'.', name")
		public TokenFactory classreftail;
		@Rule("{classreftail}")
		public TokenFactory classreftaillst;
		@Rule("name, [classreftaillst]")
		public TokenFactory classref;
		@Rule("ppclass, '(', classref, ')', '.', name, actuallist")
		public TokenFactory classmethod;
		
		@Rule("\"$SYSTEM\":1")
		public TokenFactory system;
		@Rule("'.', name")
		public TokenFactory method;
		@Rule("{method}")
		public TokenFactory methods;
		@Rule("system, [methods], actuallist")
		public TokenFactory systemcall;
		
		@Rule("fanoutlabel")
		public TokenFactory doobjectname;
		@Rule("fanoutlabel, method, [dolineoffset], [doroutineref], [actuallist], [postcondition]")
		public TokenFactory objdoargument;
		@Rule("classmethod, [dolineoffset], [doroutineref], [actuallist], [postcondition]")
		public TokenFactory clsdoargument;
		@Rule("systemcall, [dolineoffset], [doroutineref], [actuallist], [postcondition]")
		public TokenFactory sysdoargument;
		@TokenType(MDoArgument.class)	
		@Rule("fanoutlabel, ['^', ((envfanoutroutine | objdoroutine | fanoutroutine) , [actuallist]) | indfanoutroutine], [postcondition]")
		public TokenFactory doargument;

		@Rule("objdoargument | extdoargument | inddoargument | offsetdoargument | labelcalldoargument | doargument | onlyrsimpledoargument | onlyrdoargument | clsdoargument | sysdoargument")
		public TokenFactory doargumentall;
		
		@Rule("name, method")
		public TokenFactory objdoroutine;
		@Rule("envfanoutroutine | objdoroutine | fanoutroutine | indfanoutroutine")
		public TokenFactory doroutinepostcaret;
		
		@TokenType(MExtrinsic.class)	
		@Rule("fanoutlabel, ['^', ((envfanoutroutine | objdoroutine | fanoutroutine) , [actuallist]) | indfanoutroutine]")
		public TokenFactory exargument;
		
		
		
		
		@Rule("classmethod | expr")
		public TokenFactory setrhs;
		
		@Rule("expr, ',', {([expr], [':', expr]):','}")
		public TokenFactory dcasearg;
	
		@Rule("{actual:','}")
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
