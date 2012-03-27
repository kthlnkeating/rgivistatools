package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandReadTest {
	@Test
	public void test() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "R !,\"Select DEBTOR NAME or BILL NUMBER: \",X:DTIME");
	}
}