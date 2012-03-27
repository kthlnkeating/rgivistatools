package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFNumLit;

public class TFNumLitTest {
	@Test
	public void test() {
		TFNumLit f = TFNumLit.getInstance();
		TFCommonTest.validCheck(f, ".11");
		TFCommonTest.validCheck(f, "1.11");
		TFCommonTest.validCheck(f, "-3.11");
		TFCommonTest.validCheck(f, ".11E12");
	}
}
