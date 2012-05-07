package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;

public class TFPatternTest {
	private void test(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		TokenFactory f = m.pattern;
		TFCommonTest.validCheck(f, "1\"C-\".E");
		TFCommonTest.validCheck(f, "1\"C-\".E ","1\"C-\".E");
		TFCommonTest.validCheck(f, ".P1N.NP");
		TFCommonTest.validCheck(f, ".P1N.NP ", ".P1N.NP");		
		TFCommonTest.validCheck(f, "1.N");		
		TFCommonTest.validCheck(f, "1(1N)");
		TFCommonTest.validCheck(f, "1N.E");		
		TFCommonTest.validCheck(f, "1(1N,1E)");		
		TFCommonTest.validCheck(f, "1\".\".E");		
		TFCommonTest.validCheck(f, "1(1\".\")");		
		TFCommonTest.validCheck(f, "1(1N,1\".\")");		
		TFCommonTest.validCheck(f, "1(1N.E,1A)");		
		TFCommonTest.validCheck(f, "1(1N.E,1\".\")");		
		TFCommonTest.validCheck(f, "1(1N.E,1\".\".E)");		
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
