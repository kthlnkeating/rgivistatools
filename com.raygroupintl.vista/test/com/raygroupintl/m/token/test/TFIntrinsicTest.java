package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.vista.struct.MError;

public class TFIntrinsicTest {
	private void testTFIntrinsic(MVersion version) {
		TokenFactory f = MTFSupply.getInstance(version).intrinsic;
		TFCommonTest.validCheck(f, "$P(LST,\",\",FLD)");		
		TFCommonTest.validCheck(f, "$S(LST=\"A\":0,1:1)");		
		TFCommonTest.validCheck(f, "$S(A>$$A^B:0,1:1)");		
		TFCommonTest.errorCheck(f, "$XX(LST=\"A\":0,1:1)", MError.ERR_UNKNOWN_INTRINSIC_FUNCTION);
		TFCommonTest.errorCheck(f, "$V(#46C,-3,4)", MError.ERR_GENERAL_SYNTAX);
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
