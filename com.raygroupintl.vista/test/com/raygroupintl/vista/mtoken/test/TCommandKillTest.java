package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandKillTest {
	@Test
	public void test() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "K A");
		TFCommonTest.validCheck(f, "K A,B,@C,D");
		TFCommonTest.validCheck(f, "K @A");
		TFCommonTest.validCheck(f, "K @A,@C");
		TFCommonTest.validCheck(f, "K (A,B),D,(R,E)");
		TFCommonTest.validCheck(f, "K A,B");
		TFCommonTest.validCheck(f, "K CC,DD,EE");
		TFCommonTest.validCheck(f, "K %ZIS");
	}
}
