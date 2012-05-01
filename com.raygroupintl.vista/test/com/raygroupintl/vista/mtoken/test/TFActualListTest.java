package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.MTFSupply;
import com.raygroupintl.vista.mtoken.MVersion;

public class TFActualListTest {
	private void test(MVersion version) {
		ITokenFactory f = MTFSupply.getInstance(version).actuallist;
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
