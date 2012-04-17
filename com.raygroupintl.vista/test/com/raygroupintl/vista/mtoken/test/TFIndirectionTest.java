package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFIndirection;

public class TFIndirectionTest {
	private void test(MVersion version) {
		TFIndirection f = TFIndirection.getInstance(version);		
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
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
