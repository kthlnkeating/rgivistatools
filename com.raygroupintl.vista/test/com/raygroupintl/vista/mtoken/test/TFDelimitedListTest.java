package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFActual;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFExpr;

public class TFDelimitedListTest {
	private void testExpr(MVersion version) {
		TFDelimitedList f = TFDelimitedList.getInstance(TFExpr.getInstance(version), ',');
		TFCommonTest.validCheck(f, "C'>3");
		TFCommonTest.validCheck(f, "C'>3,B>1");
		TFCommonTest.validCheck(f, "C'>3,A=3,B]]1");
	}
	
	@Test
	public void testExpr() {
		testExpr(MVersion.CACHE);
		testExpr(MVersion.ANSI_STD_95);
	}

	private void testActual(MVersion version) {
		TFDelimitedList f = TFDelimitedList.getInstance(TFActual.getInstance(version), ',');
		TFCommonTest.validCheck(f, "LST,\",\",FLD");
	}

	@Test
	public void testActual() {
		testActual(MVersion.CACHE);
		testActual(MVersion.ANSI_STD_95);
	}
}
