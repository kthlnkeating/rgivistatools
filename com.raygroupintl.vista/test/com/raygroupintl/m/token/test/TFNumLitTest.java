package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.m.token.TFNumLit;

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
