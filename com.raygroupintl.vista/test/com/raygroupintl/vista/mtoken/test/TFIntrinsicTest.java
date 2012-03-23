package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFIntrinsic;

public class TFIntrinsicTest {
	@Test
	public void test() {
		TFIntrinsic f = TFIntrinsic.getInstance();
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
	}
}
