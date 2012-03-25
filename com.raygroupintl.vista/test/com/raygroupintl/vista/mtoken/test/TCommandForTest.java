package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.command.TCommandFor;

public class TCommandForTest {
	private void testCommon(String v) {
		TCommandFor o = new TCommandFor("F");
		IToken t = o.getArgument(v, 0);
		TFCommonTest.validCheck(t, v);		
	}
	
	@Test
	public void test() {
		this.testCommon("FLD=1:1:$L(LST,\",\")");
	}
}