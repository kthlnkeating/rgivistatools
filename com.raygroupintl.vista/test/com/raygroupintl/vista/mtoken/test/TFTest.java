package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFActual;
import com.raygroupintl.vista.mtoken.TFExprItem;
import com.raygroupintl.vista.mtoken.TFGvn;
import com.raygroupintl.vista.mtoken.TFGvnAll;

public class TFTest {
	public void testTFGvn(MVersion version) {
		TFGvn f = TFGvn.getInstance(version);
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
	}

	@Test
	public void testTFGvn() {
		testTFGvn(MVersion.CACHE);
		testTFGvn(MVersion.ANSI_STD_95);		
	}

	public void testTFGvnAll(MVersion version) {
		TFGvnAll f = TFGvnAll.getInstance(version);
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
		TFActual f = TFActual.getInstance(version);
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

	public void testTFExprItem(MVersion version) {
		TFExprItem f = TFExprItem.getInstance(version);
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
}
