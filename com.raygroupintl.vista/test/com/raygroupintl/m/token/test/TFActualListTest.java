package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;

public class TFActualListTest {
	private void test(MVersion version) {
		TokenFactory f = MTFSupply.getInstance(version).actuallist;
		TFCommonTest.validCheck(f, "(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "(.LST,.5,FLD)");		
		TFCommonTest.validCheck(f, "(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, "(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
