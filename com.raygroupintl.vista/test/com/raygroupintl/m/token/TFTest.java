package com.raygroupintl.m.token;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.parser.TokenFactory;

public class TFTest {
	private static MTFSupply supplyStd95;
	private static MTFSupply supplyCache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		supplyStd95 = MTFSupply.getInstance(MVersion.ANSI_STD_95);
		supplyCache = MTFSupply.getInstance(MVersion.CACHE);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		supplyStd95 = null;
		supplyCache = null;
	}

	public void testTFActual(MTFSupply m) {
		TokenFactory f = m.actual;
		TFCommonTest.validCheck(f, ".57");
		TFCommonTest.validCheck(f, ".57  ", ".57");
		TFCommonTest.validCheck(f, ".INPUT");
		TFCommonTest.validCheck(f, ".INPUT  ", ".INPUT");
		TFCommonTest.validCheck(f, "5+A-B");
		TFCommonTest.validCheck(f, "5+A-B   ", "5+A-B");
		TFCommonTest.validCheck(f, "@(\"PSBTAB\"_(FLD-1))+1");
		TFCommonTest.validCheck(f, "((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1)))");
		TFCommonTest.validCheck(f, ".@VAR");
	}
	
	@Test
	public void testTFActual() {
		testTFActual(supplyCache);
		testTFActual(supplyStd95);		
	}

	private void testActualList(MTFSupply m) {
		TokenFactory f = m.actuallist;
		TFCommonTest.validCheck(f, "()");		
		TFCommonTest.validCheck(f, "(C'>3)");		
		TFCommonTest.validCheck(f, "(C'>3,B>1)");		
		TFCommonTest.validCheck(f, "(C'>3,A=3,B]]1)");		
		TFCommonTest.validCheck(f, "(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "(.LST,.5,FLD)");		
		TFCommonTest.validCheck(f, "(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, "(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
	}

	@Test
	public void testActualList() {
		testActualList(supplyCache);
		testActualList(supplyStd95);
	}

	public void testTFComment(MTFSupply m) {
		TokenFactory f = m.comment;
		TFCommonTest.validCheck(f, ";", false);
		TFCommonTest.validCheck(f, "; this is a comment", false);
		TFCommonTest.nullCheck(f, "this is a comment");
		TFCommonTest.validCheck(f, "; comment\n", "; comment");
		TFCommonTest.validCheck(f, "; comment\n  ", "; comment");
		TFCommonTest.validCheck(f, "; comment\r\n", "; comment");
		TFCommonTest.validCheck(f, "; comment\r\n  ", "; comment");
	}

	@Test
	public void testTFComment() {
		testTFComment(supplyCache);
		testTFComment(supplyStd95);		
	}
		
	public void testTFEnvironment(MTFSupply m) {
		TokenFactory f = m.environment;
		TFCommonTest.validCheck(f, "|A|");
		TFCommonTest.validCheck(f, "|A+B|");
		TFCommonTest.validCheck(f, "[A]");
		TFCommonTest.validCheck(f, "[A,B]");
		TFCommonTest.validCheck(f, "[A,\"B\"]");
		TFCommonTest.errorCheck(f, "||", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, "[A,B", MError.ERR_GENERAL_SYNTAX, 4);
		TFCommonTest.errorCheck(f, "[]", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, "[A+B]", MError.ERR_GENERAL_SYNTAX, 2);
	}

	@Test
	public void testTFEnvironment() {
		testTFEnvironment(supplyCache);
		testTFEnvironment(supplyStd95);		
	}
		
	public void TFDeviceParams(MTFSupply m) {
		TokenFactory f = m.deviceparams;
		TFCommonTest.validCheck(f, "(:XOBPORT:\"AT\")");
	}
	
	@Test
	public void testTFDeviceParams() {
		TFDeviceParams(supplyCache);
		TFDeviceParams(supplyStd95);		
	}

	public void testTFExtDoArgument(MTFSupply m) {
		TokenFactory f = m.extdoargument;
		TFCommonTest.validCheck(f, "&ROUTINE");
		TFCommonTest.validCheck(f, "&ROUTINE(P0,\"RGI\",13)");
		TFCommonTest.validCheck(f, "&%^R5");
		TFCommonTest.validCheck(f, "&T1^ROUTINE(P0,,.P2)");
		TFCommonTest.validCheck(f, "&P0.ROUTINE");
		TFCommonTest.validCheck(f, "&P1.ROUTINE(P0,\"RGI\",13)");
		TFCommonTest.validCheck(f, "&P2.%^R5");
		TFCommonTest.validCheck(f, "&P3.T1^ROUTINE(P0,,.P2)");
		TFCommonTest.nullCheck(f, "^ROUTINE");
		TFCommonTest.errorCheck(f, "&&", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, "&RO(P0,", MError.ERR_GENERAL_SYNTAX, 7);
		TFCommonTest.errorCheck(f, "&RO..A,", MError.ERR_GENERAL_SYNTAX, 4);
		TFCommonTest.errorCheck(f, "&RO.(A),", MError.ERR_GENERAL_SYNTAX, 4);
	}

	@Test
	public void testTFExtDoArgument() {
		testTFExtDoArgument(supplyCache);
		testTFExtDoArgument(supplyStd95);		
	}
	
	public void testTFExternal(MTFSupply m) {
		TokenFactory f = m.external;
		TFCommonTest.validCheck(f, "$&ZLIB.%GETDVI(%XX,\"DEVCLASS\")");
	}

	@Test
	public void testTFExternal() {
		testTFExternal(supplyCache);
		testTFExternal(supplyStd95);		
	}

	private void testTFExpr(MTFSupply m) {
		TokenFactory f = m.expr;
		TFCommonTest.validCheck(f, "^A");
		TFCommonTest.validCheck(f, "@^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheck(f, "^A(1)");
		TFCommonTest.validCheck(f, "C'>3");
		TFCommonTest.validCheck(f, "^YTT(601,YSTEST,\"G\",L,1,1,0)");
		TFCommonTest.validCheck(f, "IOST?1\"C-\".E");
		TFCommonTest.validCheck(f, "IOST?1\"C-\".E ", "IOST?1\"C-\".E");
		TFCommonTest.validCheck(f, "LST");
		TFCommonTest.validCheck(f, "\",\"");
		TFCommonTest.validCheck(f, "FLD");
		TFCommonTest.validCheck(f, "$L($T(NTRTMSG^HDISVAP))");
		TFCommonTest.validCheck(f, "@CLIN@(0)=0");
		TFCommonTest.validCheck(f, "$P(LA7XFORM,\"^\")?1.N");
		TFCommonTest.validCheck(f, "LA7VAL?1(1N.E,1\".\".E)");
		TFCommonTest.validCheck(f, "$D(@G)#10");
		TFCommonTest.validCheck(f, "$O(^$ROUTINE(ROU))");
		TFCommonTest.validCheck(f, "@SCLIST@(0)>0");
	}

	@Test
	public void testTFExpr() {
		testTFExpr(supplyCache);
		testTFExpr(supplyStd95);
	}

	public void testTFExprItem(MTFSupply m) {
		TokenFactory f = m.expritem;
		TFCommonTest.validCheck(f, "$$TEST(A)");
		TFCommonTest.validCheck(f, "$$TEST^DOHA");
		TFCommonTest.validCheck(f, "$$TEST");
		TFCommonTest.validCheck(f, "$$TEST^DOHA(A,B)");
		TFCommonTest.validCheck(f, "$$MG^XMBGRP(\"RCCPC STATEMENTS\",0,.5,1,\"\",.DES,1)");
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "+$P(LST,\",\",FLD)");
		TFCommonTest.validCheck(f, "$$AB^VC()");
		TFCommonTest.validCheck(f, "$$AB^VC");
		TFCommonTest.validCheck(f, "$$@AB^VC");
		TFCommonTest.validCheck(f, "$$@AB^@VC");
		TFCommonTest.validCheck(f, "$$AB^@VC");
		TFCommonTest.validCheck(f, "$T(NTRTMSG^HDISVAP)");
		TFCommonTest.validCheck(f, "$T(+3)");
		TFCommonTest.validCheck(f, "0");
		TFCommonTest.validCheck(f, "$$STOREVAR^HLEME(EVENT,.@VAR,VAR)");
	}

	@Test
	public void testTFExprItem() {
		testTFExprItem(supplyCache);
		testTFExprItem(supplyStd95);		
	}

	public void testTFGvn(MTFSupply m) {
		TokenFactory f = m.gvn;
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
	}

	@Test
	public void testTFGvn() {
		testTFGvn(supplyCache);
		testTFGvn(supplyStd95);		
	}

	public void testTFGvnAll(MTFSupply m) {
		TokenFactory f = m.gvnall;
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^$ROUTINE(ROU)");
		TFCommonTest.validCheck(f, "^[ZTM,ZTN]%ZTSCH");
		TFCommonTest.validCheck(f, "^$W(\"ZISGTRM\")");
	}

	@Test
	public void testTFGvnAll() {
		testTFGvnAll(supplyCache);
		testTFGvnAll(supplyStd95);		
	}

	private void testTFIndirection(MTFSupply m) {
		TokenFactory f = m.indirection;		
		TFCommonTest.validCheck(f, "@A");
		TFCommonTest.validCheck(f, "@(+$P(LST,\",\",FLD))");
		TFCommonTest.validCheck(f, "@H@(0)");
		TFCommonTest.validCheck(f, "@XARRAY@(FROMX1,TO1)");
		TFCommonTest.validCheck(f, "@RCVAR@(Z,\"\")");
		TFCommonTest.validCheck(f, "@RCVAR@(Z,\"*\")");
		TFCommonTest.validCheck(f, "@CLIN@(0)");
		TFCommonTest.validCheck(f, "@(\"PSBTAB\"_(FLD-1))");
		TFCommonTest.validCheck(f, "@SCLIST@(0)");
	}
	
	@Test
	public void testTFIndirection() {
		testTFIndirection(supplyCache);
		testTFIndirection(supplyStd95);
	}

	public void testTFLvn(MTFSupply m) {
		TokenFactory f = m.lvn;
		TFCommonTest.validCheck(f, "A");
	}

	@Test
	public void testTFLvn() {
		testTFLvn(supplyCache);
		testTFLvn(supplyStd95);		
	}

	public void testTFName(MTFSupply m) {
		TokenFactory f = m.name;
		TFCommonTest.validCheck(f, "RGI3");
		TFCommonTest.validCheck(f, "%RGI");
		TFCommonTest.validCheck(f, "rgi");
		TFCommonTest.validCheck(f, "%rgi");
		TFCommonTest.validCheck(f, "rGi5");
		TFCommonTest.validCheck(f, "%rGi5");
		TFCommonTest.nullCheck(f, "2RGI");
		TFCommonTest.nullCheck(f, ":RGI");
		TFCommonTest.validCheck(f, "%%", "%");
		TFCommonTest.validCheck(f, "%RGI%", "%RGI");
	}

	@Test
	public void testTFName() {
		testTFName(supplyCache);
		testTFName(supplyStd95);		
	}

	public void testTFNumLit(MTFSupply m) {
		TokenFactory f = m.numlit;
		TFCommonTest.validCheck(f, ".11");
		TFCommonTest.validCheck(f, "1.11");
		TFCommonTest.validCheck(f, "-3.11");
		TFCommonTest.validCheck(f, ".11E12");
		TFCommonTest.errorCheck(f, "1.E12", MError.ERR_GENERAL_SYNTAX, 2);
		TFCommonTest.errorCheck(f, "1.E-12", MError.ERR_GENERAL_SYNTAX, 2);
		TFCommonTest.errorCheck(f, "1.E+12", MError.ERR_GENERAL_SYNTAX, 2);
	}

	@Test
	public void testTFNumLit() {
		testTFNumLit(supplyCache);
		testTFNumLit(supplyStd95);		
	}

	public void testTFStringLiteral(MTFSupply m) {
		TokenFactory f = m.strlit;
		TFCommonTest.validCheck(f, "\"This is a comment\"");
		TFCommonTest.validCheck(f, "\"Comment with quotes \"\" one\"");
		TFCommonTest.validCheck(f, "\"Comment with quotes \"\" one \"\" two\"");
		TFCommonTest.validCheck(f, "\"Comment with quotes \"\" one \"\" two and end \"\"\"");
		TFCommonTest.validCheck(f, "\"\"\"\"\"\"");
		TFCommonTest.errorCheck(f, "\" unmatched", MError.ERR_GENERAL_SYNTAX, 11);
		TFCommonTest.errorCheck(f, "\" unmatched \"\" one", MError.ERR_GENERAL_SYNTAX, 18);
		TFCommonTest.errorCheck(f, "\" unmatched \"\" one \"\" two", MError.ERR_GENERAL_SYNTAX, 25);
	}
	
	@Test
	public void testTFStringLiteral() {
		testTFStringLiteral(supplyCache);
		testTFStringLiteral(supplyStd95);		
	}

	private void testPattern(MTFSupply m) {
		TokenFactory f = m.pattern;
		TFCommonTest.validCheck(f, "1\"C-\".E");
		TFCommonTest.validCheck(f, "1\"C-\".E ","1\"C-\".E");
		TFCommonTest.validCheck(f, ".P1N.NP");
		TFCommonTest.validCheck(f, ".P1N.NP ", ".P1N.NP");		
		TFCommonTest.validCheck(f, "1.N");		
		TFCommonTest.validCheck(f, "1(1N)");
		TFCommonTest.validCheck(f, "1N.E");		
		TFCommonTest.validCheck(f, "1(1N,1E)");		
		TFCommonTest.validCheck(f, "1\".\".E");		
		TFCommonTest.validCheck(f, "1(1\".\")");		
		TFCommonTest.validCheck(f, "1(1N,1\".\")");		
		TFCommonTest.validCheck(f, "1(1N.E,1A)");		
		TFCommonTest.validCheck(f, "1(1N.E,1\".\")");		
		TFCommonTest.validCheck(f, "1(1N.E,1\".\".E)");		
	}

	@Test
	public void testPattern() {
		testPattern(supplyCache);
		testPattern(supplyStd95);
	}
}
