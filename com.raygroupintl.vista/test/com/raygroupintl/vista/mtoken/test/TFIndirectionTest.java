package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFIndirection;

public class TFIndirectionTest {
	@Test
	public void test() {
		TFIndirection f = TFIndirection.getInstance();		
		TFCommonTest.validCheck(f, "@(+$P(LST,\",\",FLD))");
	}
}
