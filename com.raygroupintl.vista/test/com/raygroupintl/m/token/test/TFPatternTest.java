package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFPattern;

public class TFPatternTest {
	private void test(MVersion version) {
		TFPattern f = TFPattern.getInstance(version);
		TFCommonTest.validCheck(f, "1\"C-\".E");
		TFCommonTest.validCheck(f, "1\"C-\".E ","1\"C-\".E");
		TFCommonTest.validCheck(f, ".P1N.NP");
		TFCommonTest.validCheck(f, ".P1N.NP ", ".P1N.NP");		
		TFCommonTest.validCheck(f, "1.N");		
		TFCommonTest.validCheck(f, "1(1N.E,1\".\".E)");		
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
