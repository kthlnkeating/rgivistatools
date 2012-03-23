package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFActual;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;

public class TFDelimitedListTest {
	@Test
	public void testExpr() {
		TFDelimitedList f = TFDelimitedList.getInstance(TFExpr.getInstance(), ',');
		TFCommonTest.validCheck(f, "C'>3");
		TFCommonTest.validCheck(f, "C'>3,B>1");
		TFCommonTest.validCheck(f, "C'>3,A=3,B]]1");
	}
	
	@Test
	public void testActual() {
		TFDelimitedList f = TFDelimitedList.getInstance(TFActual.getInstance(), ',');
		TFCommonTest.validCheck(f, "LST,\",\",FLD");
	}
}
