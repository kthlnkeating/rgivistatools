package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandLockTest {
	@Test
	public void test() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "L -^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "L +^PRCA(430,DA,0):0");		
		TFCommonTest.validCheck(f, "L -^PRCA(430,+$G(PRCABN),0),+^PRCA:0");
	}
}
