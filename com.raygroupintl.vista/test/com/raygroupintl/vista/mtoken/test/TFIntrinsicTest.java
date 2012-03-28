package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.struct.MError;

public class TFIntrinsicTest {
	@Test
	public void test() {
		TFIntrinsic f = TFIntrinsic.getInstance();
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "$S(LST=\"A\":0,1:1)");		
		TFCommonTest.validCheck(f, "$XX(LST=\"A\":0,1:1)", MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);		
	}
}
