package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.MTFSupply;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFDelimitedList;
import com.raygroupintl.vista.mtoken.TFNumLit;

public class TFDelimitedListTest {
	private void testExpr(MVersion version) {
		TFDelimitedList f = TFDelimitedList.getInstance(MTFSupply.getInstance(version).expr, ',');
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
		MTFSupply m = MTFSupply.getInstance(version);
		ITokenFactory fActual = m.actual;
		TFDelimitedList f = TFDelimitedList.getInstance(fActual, ',');
		TFCommonTest.validCheck(f, "LST,\",\",FLD");
	}

	@Test
	public void testActual() {
		testActual(MVersion.CACHE);
		testActual(MVersion.ANSI_STD_95);
	}

	private void testTFDelimitedTest(MVersion version) {
		MTFSupply m = MTFSupply.getInstance(version);
		TFDelimitedList f = new TFDelimitedList();
		f.setElementFactory(m.actual);
		f.setLeft("(");
		f.setRight(")");
		f.setDelimiter(",");
		f.setAllowEmpty(true);
		TFCommonTest.validCheck(f, "()");
		TFCommonTest.validCheck(f, "(,A,)");
		f.setDelimiter(":");
		f.setElementFactory(TFNumLit.getInstance());
		TFCommonTest.validCheck(f, "(1)");
		TFCommonTest.validCheck(f, "(:1:1)");
	}

	@Test
	public void testTFDelimitedTest() {
		testTFDelimitedTest(MVersion.CACHE);
		testTFDelimitedTest(MVersion.ANSI_STD_95);
	}


}
