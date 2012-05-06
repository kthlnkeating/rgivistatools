package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFStringLiteral;
import com.raygroupintl.vista.struct.MError;

public class TFTest {
	public void testTFEnvironment(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.environment;
		TFCommonTest.validCheck(f, "|A|");
		TFCommonTest.validCheck(f, "[A,B]");
		TFCommonTest.errorCheck(f, "||", MError.ERR_GENERAL_SYNTAX);
		TFCommonTest.errorCheck(f, "[A,B", MError.ERR_GENERAL_SYNTAX);
		TFCommonTest.errorCheck(f, "[]", MError.ERR_GENERAL_SYNTAX);
	}

	@Test
	public void testTFEnvironment() {
		testTFEnvironment(MVersion.CACHE);
		testTFEnvironment(MVersion.ANSI_STD_95);		
	}
		
	public void testTFExternal(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.external;
		TFCommonTest.validCheck(f, "$&ZLIB.%GETDVI(%XX,\"DEVCLASS\")");
	}

	@Test
	public void testTFExternal() {
		testTFExternal(MVersion.CACHE);
		testTFExternal(MVersion.ANSI_STD_95);		
	}

	public void testTFGvn(MVersion version) {
		ITokenFactory f = MTFSupply.getInstance(version).gvn;
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
	}

	private void testTFExpr(MVersion version) {
		ITokenFactory f = MTFSupply.getInstance(version).expr;
		TFCommonTest.validCheck(f, "@^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheck(f, "^A");
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
		testTFExpr(MVersion.CACHE);
		testTFExpr(MVersion.ANSI_STD_95);
	}

	@Test
	public void testTFGvn() {
		testTFGvn(MVersion.CACHE);
		testTFGvn(MVersion.ANSI_STD_95);		
	}

	public void testTFGvnAll(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.gvnall;
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^$ROUTINE(ROU)");
		TFCommonTest.validCheck(f, "^[ZTM,ZTN]%ZTSCH");
		TFCommonTest.validCheck(f, "^$W(\"ZISGTRM\")");
	}

	@Test
	public void testTFGvnAll() {
		testTFGvnAll(MVersion.CACHE);
		testTFGvnAll(MVersion.ANSI_STD_95);		
	}

	public void testTFActual(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.actual;
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
		testTFActual(MVersion.CACHE);
		testTFActual(MVersion.ANSI_STD_95);		
	}

	private void testTFIndirection(MVersion version) {
		ITokenFactory f = MTFSupply.getInstance(version).indirection;		
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
		testTFIndirection(MVersion.CACHE);
		testTFIndirection(MVersion.ANSI_STD_95);
	}

	public void TFDeviceParams(MVersion version) {
		ITokenFactory f = MTFSupply.getInstance(version).deviceparams;
		TFCommonTest.validCheck(f, "(:XOBPORT:\"AT\")");
	}
	
	@Test
	public void testTFDeviceParams() {
		TFDeviceParams(MVersion.CACHE);
		TFDeviceParams(MVersion.ANSI_STD_95);		
	}

	public void testTFExprItem(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.expritem;
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
		testTFExprItem(MVersion.CACHE);
		testTFExprItem(MVersion.ANSI_STD_95);		
	}

	public void testTFNumLit(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory f = m.numlit;
		TFCommonTest.validCheck(f, ".11");
		TFCommonTest.validCheck(f, "1.11");
		TFCommonTest.validCheck(f, "-3.11");
		TFCommonTest.validCheck(f, ".11E12");
		TFCommonTest.errorCheck(f, "1.E12");
		TFCommonTest.errorCheck(f, "1.E-12");
		TFCommonTest.errorCheck(f, "1.E+12");
	}

	@Test
	public void testTFNumLit() {
		testTFNumLit(MVersion.CACHE);
		testTFNumLit(MVersion.ANSI_STD_95);		
	}

	@Test
	public void testTFStringLiteral() {
		TFStringLiteral f = TFStringLiteral.getInstance();
		TFCommonTest.validCheck(f, "\"\"\"\"\"\"");
	}
}
