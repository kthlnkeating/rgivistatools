package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFExpr;

public class TFExprTest {
	@Test
	public void test() {
		TFExpr f = TFExpr.getInstance();
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
	}
}
