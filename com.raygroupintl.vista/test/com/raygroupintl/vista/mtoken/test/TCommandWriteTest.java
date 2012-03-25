package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.command.TCommandWrite;

public class TCommandWriteTest {
	private void testCommon(String v) {
		TCommandWrite o = new TCommandWrite("W");
		IToken t = o.getArgument(v, 0);
		TFCommonTest.validCheck(t, v);		
	}
	
	@Test
	public void test() {
		this.testCommon("!!,^YTT(601,YSTEST,\"G\",L,1,1,0)");
	}
}
