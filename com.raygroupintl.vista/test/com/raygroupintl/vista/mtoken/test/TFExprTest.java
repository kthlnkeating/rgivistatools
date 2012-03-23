package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFExpr;

public class TFExprTest {
	@Test
	public void test() {
		TFExpr f = TFExpr.getInstance();
		TFCommonTest.validCheck(f, "C'>3");
	}
}
