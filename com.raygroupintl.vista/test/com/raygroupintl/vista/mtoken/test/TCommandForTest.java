package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandForTest {
	@Test
	public void test() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "F FLD=1:1:$L(LST,\",\")");
		TFCommonTest.validCheck(f, "F STAT=42,16");
	}
}