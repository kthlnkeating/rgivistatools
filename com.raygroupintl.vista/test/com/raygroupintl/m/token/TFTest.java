package com.raygroupintl.m.token;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFTest {
	private static MTFSupply supplyStd95;
	private static MTFSupply supplyCache;
	private static AdapterSupply adapterSupply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		supplyStd95 = MTFSupply.getInstance(MVersion.ANSI_STD_95);
		supplyCache = MTFSupply.getInstance(MVersion.CACHE);
		adapterSupply = new MAdapterSupply();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		supplyStd95 = null;
		supplyCache = null;
		adapterSupply = null;
	}

	public void testTFActual(MTFSupply m) {
		TokenFactory f = m.actual;
		TFCommonTest.validCheck(f, adapterSupply, ".57");
		TFCommonTest.validCheck(f, adapterSupply, ".57  ", ".57");
		TFCommonTest.validCheck(f, adapterSupply, ".INPUT");
		TFCommonTest.validCheck(f, adapterSupply, ".INPUT  ", ".INPUT");
		TFCommonTest.validCheck(f, adapterSupply, "5+A-B");
		TFCommonTest.validCheck(f, adapterSupply, "5+A-B   ", "5+A-B");
		TFCommonTest.validCheck(f, adapterSupply, "@(\"PSBTAB\"_(FLD-1))+1");
		TFCommonTest.validCheck(f, adapterSupply, "((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1)))");
		TFCommonTest.validCheck(f, adapterSupply, ".@VAR");
	}
	
	@Test
	public void testTFActual() {
		testTFActual(supplyCache);
		testTFActual(supplyStd95);		
	}

	private void testActualList(MTFSupply m) {
		TokenFactory f = m.actuallist;
		TFCommonTest.validCheck(f, adapterSupply, "()");		
		TFCommonTest.validCheck(f, adapterSupply, "(C'>3)");		
		TFCommonTest.validCheck(f, adapterSupply, "(C'>3,B>1)");		
		TFCommonTest.validCheck(f, adapterSupply, "(C'>3,A=3,B]]1)");		
		TFCommonTest.validCheck(f, adapterSupply, "(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, adapterSupply, "(.LST,.5,FLD)");		
		TFCommonTest.validCheck(f, adapterSupply, "(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, adapterSupply, "(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
	}

	@Test
	public void testActualList() {
		testActualList(supplyCache);
		testActualList(supplyStd95);
	}

	public void testTFComment(MTFSupply m) {
		TokenFactory f = m.comment;
		TFCommonTest.validCheck(f, adapterSupply, ";", false);
		TFCommonTest.validCheck(f, adapterSupply, "; this is a comment", false);
		TFCommonTest.nullCheck(f, adapterSupply, "this is a comment");
		TFCommonTest.validCheck(f, adapterSupply, "; comment\n", "; comment");
		TFCommonTest.validCheck(f, adapterSupply, "; comment\n  ", "; comment");
		TFCommonTest.validCheck(f, adapterSupply, "; comment\r\n", "; comment");
		TFCommonTest.validCheck(f, adapterSupply, "; comment\r\n  ", "; comment");
	}

	@Test
	public void testTFComment() {
		testTFComment(supplyCache);
		testTFComment(supplyStd95);		
	}
		
	public void testTFEnvironment(MTFSupply m) {
		TokenFactory f = m.environment;
		TFCommonTest.validCheck(f, adapterSupply, "|A|");
		TFCommonTest.validCheck(f, adapterSupply, "|A+B|");
		TFCommonTest.validCheck(f, adapterSupply, "[A]");
		TFCommonTest.validCheck(f, adapterSupply, "[A,B]");
		TFCommonTest.validCheck(f, adapterSupply, "[A,\"B\"]");
		TFCommonTest.errorCheck(f, adapterSupply, "||", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, adapterSupply, "[A,B", MError.ERR_GENERAL_SYNTAX, 4);
		TFCommonTest.errorCheck(f, adapterSupply, "[]", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, adapterSupply, "[A+B]", MError.ERR_GENERAL_SYNTAX, 2);
	}

	@Test
	public void testTFEnvironment() {
		testTFEnvironment(supplyCache);
		testTFEnvironment(supplyStd95);		
	}
		
	public void TFDeviceParams(MTFSupply m) {
		TokenFactory f = m.deviceparams;
		TFCommonTest.validCheck(f, adapterSupply, "(:XOBPORT:\"AT\")");
	}
	
	@Test
	public void testTFDeviceParams() {
		TFDeviceParams(supplyCache);
		TFDeviceParams(supplyStd95);		
	}

	public void testTFExtDoArgument(MTFSupply m) {
		TokenFactory f = m.extdoargument;
		TFCommonTest.validCheck(f, adapterSupply, "&ROUTINE");
		TFCommonTest.validCheck(f, adapterSupply, "&ROUTINE(P0,\"RGI\",13)");
		TFCommonTest.validCheck(f, adapterSupply, "&%^R5");
		TFCommonTest.validCheck(f, adapterSupply, "&T1^ROUTINE(P0,,.P2)");
		TFCommonTest.validCheck(f, adapterSupply, "&P0.ROUTINE");
		TFCommonTest.validCheck(f, adapterSupply, "&P1.ROUTINE(P0,\"RGI\",13)");
		TFCommonTest.validCheck(f, adapterSupply, "&P2.%^R5");
		TFCommonTest.validCheck(f, adapterSupply, "&P3.T1^ROUTINE(P0,,.P2)");
		TFCommonTest.nullCheck(f, adapterSupply, "^ROUTINE");
		TFCommonTest.errorCheck(f, adapterSupply, "&&", MError.ERR_GENERAL_SYNTAX, 1);
		TFCommonTest.errorCheck(f, adapterSupply, "&RO(P0,", MError.ERR_GENERAL_SYNTAX, 7);
		TFCommonTest.errorCheck(f, adapterSupply, "&RO..A,", MError.ERR_GENERAL_SYNTAX, 4);
		TFCommonTest.errorCheck(f, adapterSupply, "&RO.(A),", MError.ERR_GENERAL_SYNTAX, 4);
	}

	@Test
	public void testTFExtDoArgument() {
		testTFExtDoArgument(supplyCache);
		testTFExtDoArgument(supplyStd95);		
	}
	
	public void testTFDoArgument(MTFSupply m) {
		TokenFactory f = m.doargument;
		TFCommonTest.validCheck(f, adapterSupply, "T1:COND1", TDoArgument.class);
		TFCommonTest.validCheck(f, adapterSupply, "T2", TDoArgument.class);
		TFCommonTest.validCheck(f, adapterSupply, "T0", TDoArgument.class);
	}

	@Test
	public void testTFDoArgument() {
		testTFDoArgument(supplyCache);
		testTFDoArgument(supplyStd95);		
	}
	
	public void testTFDoArguments(MTFSupply m) {
		TokenFactory f = m.doarguments;
		Token t = TFCommonTest.validCheck(f, adapterSupply, "T0,T1:COND1,T2", MTDelimitedList.class);
		Assert.assertNotNull(t);
		MTDelimitedList dl = (MTDelimitedList) t;
		for (Token lt : dl) {
			Assert.assertNotNull(lt);
			Assert.assertTrue(lt instanceof TDoArgument);
		}
	}

	@Test
	public void testTFDoArguments() {
		testTFDoArguments(supplyCache);
		testTFDoArguments(supplyStd95);		
	}
	
	public void testTFExternal(MTFSupply m) {
		TokenFactory f = m.external;
		TFCommonTest.validCheck(f, adapterSupply, "$&ZLIB.%GETDVI(%XX,\"DEVCLASS\")");
	}

	@Test
	public void testTFExternal() {
		testTFExternal(supplyCache);
		testTFExternal(supplyStd95);		
	}

	private void testTFExpr(MTFSupply m) {
		TokenFactory f = m.expr;
		TFCommonTest.validCheck(f, adapterSupply, "^A");
		TFCommonTest.validCheck(f, adapterSupply, "@^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheck(f, adapterSupply, "^A(1)");
		TFCommonTest.validCheck(f, adapterSupply, "C'>3");
		TFCommonTest.validCheck(f, adapterSupply, "^YTT(601,YSTEST,\"G\",L,1,1,0)");
		TFCommonTest.validCheck(f, adapterSupply, "IOST?1\"C-\".E");
		TFCommonTest.validCheck(f, adapterSupply, "IOST?1\"C-\".E ", "IOST?1\"C-\".E");
		TFCommonTest.validCheck(f, adapterSupply, "LST");
		TFCommonTest.validCheck(f, adapterSupply, "\",\"");
		TFCommonTest.validCheck(f, adapterSupply, "FLD");
		TFCommonTest.validCheck(f, adapterSupply, "$L($T(NTRTMSG^HDISVAP))");
		TFCommonTest.validCheck(f, adapterSupply, "@CLIN@(0)=0");
		TFCommonTest.validCheck(f, adapterSupply, "$P(LA7XFORM,\"^\")?1.N");
		TFCommonTest.validCheck(f, adapterSupply, "LA7VAL?1(1N.E,1\".\".E)");
		TFCommonTest.validCheck(f, adapterSupply, "$D(@G)#10");
		TFCommonTest.validCheck(f, adapterSupply, "$O(^$ROUTINE(ROU))");
		TFCommonTest.validCheck(f, adapterSupply, "@SCLIST@(0)>0");
	}

	@Test
	public void testTFExpr() {
		testTFExpr(supplyCache);
		testTFExpr(supplyStd95);
	}

	public void testTFExprItem(MTFSupply m) {
		TokenFactory f = m.expritem;
		TFCommonTest.validCheck(f, adapterSupply, "$$TEST(A)");
		TFCommonTest.validCheck(f, adapterSupply, "$$TEST^DOHA");
		TFCommonTest.validCheck(f, adapterSupply, "$$TEST");
		TFCommonTest.validCheck(f, adapterSupply, "$$TEST^DOHA(A,B)");
		TFCommonTest.validCheck(f, adapterSupply, "$$MG^XMBGRP(\"RCCPC STATEMENTS\",0,.5,1,\"\",.DES,1)");
		TFCommonTest.validCheck(f, adapterSupply, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, adapterSupply, "+$P(LST,\",\",FLD)");
		TFCommonTest.validCheck(f, adapterSupply, "$$AB^VC()");
		TFCommonTest.validCheck(f, adapterSupply, "$$AB^VC");
		TFCommonTest.validCheck(f, adapterSupply, "$$@AB^VC");
		TFCommonTest.validCheck(f, adapterSupply, "$$@AB^@VC");
		TFCommonTest.validCheck(f, adapterSupply, "$$AB^@VC");
		TFCommonTest.validCheck(f, adapterSupply, "$T(NTRTMSG^HDISVAP)");
		TFCommonTest.validCheck(f, adapterSupply, "$T(+3)");
		TFCommonTest.validCheck(f, adapterSupply, "0");
		TFCommonTest.validCheck(f, adapterSupply, "$$STOREVAR^HLEME(EVENT,.@VAR,VAR)");
	}

	@Test
	public void testTFExprItem() {
		testTFExprItem(supplyCache);
		testTFExprItem(supplyStd95);		
	}

	public void testTFGvn(MTFSupply m) {
		TokenFactory f = m.gvn;
		TFCommonTest.validCheck(f, adapterSupply, "^PRCA(430,+$G(PRCABN),0)");
	}

	@Test
	public void testTFGvn() {
		testTFGvn(supplyCache);
		testTFGvn(supplyStd95);		
	}

	public void testTFGvnAll(MTFSupply m) {
		TokenFactory f = m.gvnall;
		TFCommonTest.validCheck(f, adapterSupply, "^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, adapterSupply, "^(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, adapterSupply, "^$ROUTINE(ROU)");
		TFCommonTest.validCheck(f, adapterSupply, "^[ZTM,ZTN]%ZTSCH");
		TFCommonTest.validCheck(f, adapterSupply, "^$W(\"ZISGTRM\")");
	}

	@Test
	public void testTFGvnAll() {
		testTFGvnAll(supplyCache);
		testTFGvnAll(supplyStd95);		
	}

	private void testTFIndirection(MTFSupply m) {
		TokenFactory f = m.indirection;		
		TFCommonTest.validCheck(f, adapterSupply, "@A");
		TFCommonTest.validCheck(f, adapterSupply, "@(+$P(LST,\",\",FLD))");
		TFCommonTest.validCheck(f, adapterSupply, "@H@(0)");
		TFCommonTest.validCheck(f, adapterSupply, "@XARRAY@(FROMX1,TO1)");
		TFCommonTest.validCheck(f, adapterSupply, "@RCVAR@(Z,\"\")");
		TFCommonTest.validCheck(f, adapterSupply, "@RCVAR@(Z,\"*\")");
		TFCommonTest.validCheck(f, adapterSupply, "@CLIN@(0)");
		TFCommonTest.validCheck(f, adapterSupply, "@(\"PSBTAB\"_(FLD-1))");
		TFCommonTest.validCheck(f, adapterSupply, "@SCLIST@(0)");
	}
	
	@Test
	public void testTFIndirection() {
		testTFIndirection(supplyCache);
		testTFIndirection(supplyStd95);
	}

	public void testTFLvn(MTFSupply m) {
		TokenFactory f = m.lvn;
		TFCommonTest.validCheck(f, adapterSupply, "A");
	}

	@Test
	public void testTFLvn() {
		testTFLvn(supplyCache);
		testTFLvn(supplyStd95);		
	}

	public void testTFName(MTFSupply m) {
		TokenFactory f = m.name;
		TFCommonTest.validCheck(f, adapterSupply, "RGI3");
		TFCommonTest.validCheck(f, adapterSupply, "%RGI");
		TFCommonTest.validCheck(f, adapterSupply, "rgi");
		TFCommonTest.validCheck(f, adapterSupply, "%rgi");
		TFCommonTest.validCheck(f, adapterSupply, "rGi5");
		TFCommonTest.validCheck(f, adapterSupply, "%rGi5");
		TFCommonTest.nullCheck(f, adapterSupply, "2RGI");
		TFCommonTest.nullCheck(f, adapterSupply, ":RGI");
		TFCommonTest.validCheck(f, adapterSupply, "%%", "%");
		TFCommonTest.validCheck(f, adapterSupply, "%RGI%", "%RGI");
	}

	@Test
	public void testTFName() {
		testTFName(supplyCache);
		testTFName(supplyStd95);		
	}

	public void testTFNumLit(MTFSupply m) {
		TokenFactory f = m.numlit;
		TFCommonTest.validCheck(f, adapterSupply, ".11");
		TFCommonTest.validCheck(f, adapterSupply, "1.11");
		TFCommonTest.validCheck(f, adapterSupply, "3.11");
		TFCommonTest.validCheck(f, adapterSupply, ".11E12");
		TFCommonTest.errorCheck(f, adapterSupply, "1.E12", MError.ERR_GENERAL_SYNTAX, 2);
		TFCommonTest.errorCheck(f, adapterSupply, "1.E-12", MError.ERR_GENERAL_SYNTAX, 2);
		TFCommonTest.errorCheck(f, adapterSupply, "1.E+12", MError.ERR_GENERAL_SYNTAX, 2);
	}

	@Test
	public void testTFNumLit() {
		testTFNumLit(supplyCache);
		testTFNumLit(supplyStd95);		
	}

	public void testTFStringLiteral(MTFSupply m) {
		TokenFactory f = m.strlit;
		TFCommonTest.validCheck(f, adapterSupply, "\"This is a comment\"");
		TFCommonTest.validCheck(f, adapterSupply, "\"Comment with quotes \"\" one\"");
		TFCommonTest.validCheck(f, adapterSupply, "\"Comment with quotes \"\" one \"\" two\"");
		TFCommonTest.validCheck(f, adapterSupply, "\"Comment with quotes \"\" one \"\" two and end \"\"\"");
		TFCommonTest.validCheck(f, adapterSupply, "\"\"\"\"\"\"");
		TFCommonTest.errorCheck(f, adapterSupply, "\" unmatched", MError.ERR_GENERAL_SYNTAX, 11);
		TFCommonTest.errorCheck(f, adapterSupply, "\" unmatched \"\" one", MError.ERR_GENERAL_SYNTAX, 18);
		TFCommonTest.errorCheck(f, adapterSupply, "\" unmatched \"\" one \"\" two", MError.ERR_GENERAL_SYNTAX, 25);
	}
	
	@Test
	public void testTFStringLiteral() {
		testTFStringLiteral(supplyCache);
		testTFStringLiteral(supplyStd95);		
	}

	private void testPattern(MTFSupply m) {
		TokenFactory f = m.pattern;
		TFCommonTest.validCheck(f, adapterSupply, "1\"C-\".E");
		TFCommonTest.validCheck(f, adapterSupply, "1\"C-\".E ","1\"C-\".E");
		TFCommonTest.validCheck(f, adapterSupply, ".P1N.NP");
		TFCommonTest.validCheck(f, adapterSupply, ".P1N.NP ", ".P1N.NP");		
		TFCommonTest.validCheck(f, adapterSupply, "1.N");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N)");
		TFCommonTest.validCheck(f, adapterSupply, "1N.E");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N,1E)");		
		TFCommonTest.validCheck(f, adapterSupply, "1\".\".E");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1\".\")");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N,1\".\")");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N.E,1A)");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N.E,1\".\")");		
		TFCommonTest.validCheck(f, adapterSupply, "1(1N.E,1\".\".E)");		
	}

	@Test
	public void testPattern() {
		testPattern(supplyCache);
		testPattern(supplyStd95);
	}
}
