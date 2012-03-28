package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFActualList;

public class TFActualListTest {
	@Test
	public void test() {
		TFActualList f = TFActualList.getInstance();
		TFCommonTest.validCheck(f, "(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "(.LST,.5,FLD)");		
		TFCommonTest.validCheck(f, "(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, "(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
	}
}
