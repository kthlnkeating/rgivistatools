package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFGvn;
import com.raygroupintl.vista.mtoken.TFGvnAll;

public class TFTest {
	@Test
	public void testTFGvn() {
		TFGvn f = TFGvn.getInstance();
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
	}

	@Test
	public void testTFGvnAll() {
		TFGvnAll f = TFGvnAll.getInstance();
		TFCommonTest.validCheck(f, "^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "^(430,+$G(PRCABN),0)");
	}
}
