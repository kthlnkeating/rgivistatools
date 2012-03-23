package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFExprItem;

public class TFExprItemTest {
	@Test
	public void test() {
		TFExprItem f = TFExprItem.getInstance();
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "+$P(LST,\",\",FLD)");
		TFCommonTest.validCheck(f, "$$AB^VC()");
	}
}
