package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFIntrinsic;
import com.raygroupintl.vista.struct.MError;

public class TFIntrinsicTest {
	private void testTFIntrinsic(MVersion version) {
		TFIntrinsic f = TFIntrinsic.getInstance(version);
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "$S(LST=\"A\":0,1:1)");		
		TFCommonTest.validCheck(f, "$S(A>$$A^B:0,1:1)");		
		TFCommonTest.validCheck(f, "$XX(LST=\"A\":0,1:1)", MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
		TFCommonTest.validCheck(f, "$V(#46C,-3,4)", MError.ERR_GENERAL_SYNTAX);
		TFCommonTest.validCheck(f, "$S(+Y:$$HLNAME^HLFNC($P(Y,\"^\",2)),1:\"\"\"\"\"\")");
		if (version == MVersion.CACHE) {
			TFCommonTest.validCheck(f, "$SYSTEM.Util.GetEnviron(\"SSH_CLIENT\")");					
			TFCommonTest.validCheck(f, "$SYSTEM.Util.GetEnviron()");					
			TFCommonTest.validCheck(f, "$CASE(%ZTBKBIG,0:$V(2040,0,\"3O\"),:$V($ZUTIL(40,32,4),0,4))");					
		}
	}

	@Test
	public void testTFIntrinsic() {
		testTFIntrinsic(MVersion.CACHE);
		testTFIntrinsic(MVersion.ANSI_STD_95);
	}
}
