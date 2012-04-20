package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandKillTest {
	private void test(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "K A");
		TFCommonTest.validCheck(f, "K A,B,@C,D");
		TFCommonTest.validCheck(f, "K @A");
		TFCommonTest.validCheck(f, "K @A,@C");
		TFCommonTest.validCheck(f, "K (A,B),D,(R,E)");
		TFCommonTest.validCheck(f, "K A,B");
		TFCommonTest.validCheck(f, "K CC,DD,EE");
		TFCommonTest.validCheck(f, "K %ZIS");
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
