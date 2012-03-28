package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFActual;
import com.raygroupintl.vista.mtoken.TFExprItem;
import com.raygroupintl.vista.mtoken.TFGvn;
import com.raygroupintl.vista.mtoken.TFGvnAll;

public class TFTest {
	@Test
	public void testTFGvn() {
		TFGvn f = TFGvn.getInstance();
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
	}

	@Test
	public void testTFGvnAll() {
		TFGvnAll f = TFGvnAll.getInstance();
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^$ROUTINE(ROU)");
	}

	@Test
	public void testTFActual() {
		TFActual f = TFActual.getInstance();
		TFCommonTest.validCheck(f, ".57");
		TFCommonTest.validCheck(f, ".57  ", ".57");
		TFCommonTest.validCheck(f, ".INPUT");
		TFCommonTest.validCheck(f, ".INPUT  ", ".INPUT");
		TFCommonTest.validCheck(f, "5+A-B");
		TFCommonTest.validCheck(f, "5+A-B   ", "5+A-B");
		TFCommonTest.validCheck(f, "@(\"PSBTAB\"_(FLD-1))+1");
		TFCommonTest.validCheck(f, "((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1)))");
	}

	@Test
	public void testTFExprItem() {
		TFExprItem f = TFExprItem.getInstance();
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
	}
}
