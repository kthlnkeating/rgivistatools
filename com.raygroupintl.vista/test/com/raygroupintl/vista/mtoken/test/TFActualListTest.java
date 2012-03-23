package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFActualList;

public class TFActualListTest {
	@Test
	public void test() {
		TFActualList f = TFActualList.getInstance();
		TFCommonTest.validCheck(f, "(LST,\",\",FLD)");		
	}
}
