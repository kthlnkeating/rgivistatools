package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.command.TCommandKill;

public class TCommandKillTest {
	private void testCommon(String v) {
		TCommandKill o = new TCommandKill("K");
		IToken t = o.getArgument(v, 0);
		TFCommonTest.validCheck(t, v);		
	}
	
	@Test
	public void test() {
		this.testCommon("A");
		this.testCommon("A,B,@C,D");
		this.testCommon("@A");
		this.testCommon("@A,@C");
		this.testCommon("(A,B),D,(R,E)");
		this.testCommon("A,B");
		this.testCommon("CC,DD,EE");
		this.testCommon("%ZIS");
	}
}
