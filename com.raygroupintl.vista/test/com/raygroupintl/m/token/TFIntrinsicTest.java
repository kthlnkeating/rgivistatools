package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ObjectSupply;
import com.raygroupintl.parsergen.ParseException;

public class TFIntrinsicTest {
	private void testTFIntrinsic(MVersion version) {
		try {
			TokenFactory f = MTFSupply.getInstance(version).intrinsic;
			ObjectSupply objectSupply = new MObjectSupply();
			TFCommonTest.validCheck(f, objectSupply, "$EREF");
			TFCommonTest.validCheck(f, objectSupply, "$P(LST,\",\",FLD)");		
			TFCommonTest.validCheck(f, objectSupply, "$S(LST=\"A\":0,1:1)");		
			TFCommonTest.validCheck(f, objectSupply, "$S(A>$$A^B:0,1:1)");		
			TFCommonTest.errorCheck(f, objectSupply, "$XX(LST=\"A\":0,1:1)", MError.ERR_UNKNOWN_INTRINSIC_FUNCTION, 4);
			TFCommonTest.errorCheck(f, objectSupply, "$V(#46C,-3,4)", MError.ERR_GENERAL_SYNTAX, 3);
			TFCommonTest.validCheck(f, objectSupply, "$S(+Y:$$HLNAME^HLFNC($P(Y,\"^\",2)),1:\"\"\"\"\"\")");
			if (version == MVersion.CACHE) {
				TFCommonTest.validCheck(f, objectSupply, "$SYSTEM.Util.GetEnviron(\"SSH_CLIENT\")");					
				TFCommonTest.validCheck(f, objectSupply, "$SYSTEM.Util.GetEnviron()");					
				TFCommonTest.validCheck(f, objectSupply, "$CASE(%ZTBKBIG,0:$V(2040,0,\"3O\"),:$V($ZUTIL(40,32,4),0,4))");					
			}
		} catch (ParseException pe) {
			fail("Exception: " + pe.getMessage());			
		}
	}

	@Test
	public void testTFIntrinsic() {
		testTFIntrinsic(MVersion.CACHE);
		testTFIntrinsic(MVersion.ANSI_STD_95);
	}
}
