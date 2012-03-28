package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFIndirection;

public class TFIndirectionTest {
	@Test
	public void test() {
		TFIndirection f = TFIndirection.getInstance();		
		TFCommonTest.validCheck(f, "@(+$P(LST,\",\",FLD))");
		TFCommonTest.validCheck(f, "@H@(0)");
		TFCommonTest.validCheck(f, "@XARRAY@(FROMX1,TO1)");
		TFCommonTest.validCheck(f, "@RCVAR@(Z,\"\")");
		TFCommonTest.validCheck(f, "@RCVAR@(Z,\"*\")");
		TFCommonTest.validCheck(f, "@CLIN@(0)");
		TFCommonTest.validCheck(f, "@(\"PSBTAB\"_(FLD-1))");		
	}
}
