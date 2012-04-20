package com.raygroupintl.vista.mtoken.test;

import junit.framework.Assert;

import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;
import com.raygroupintl.vista.mtoken.TFLine;

public class TFCommonTest {
	private static void validTokenCheck(IToken t, String v) {
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());		
	}
	
	static void validCheck(IToken t, String v) {
		Assert.assertFalse(t.hasError());
		validTokenCheck(t, v);
	}
			
	static void validCheck(IToken t, String v, int errorCode) {
		Assert.assertTrue(t.hasError());
		Assert.assertEquals(errorCode,  t.getErrors().get(0).getCode());
		validTokenCheck(t, v);
	}
			
	static void validCheck(ITokenFactory f, String v) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, v);
		if (! ((f instanceof TFCommand) || (f instanceof TFLine))) {
			validCheck(f, v + " ", v);
		}
		}

	static void validCheck(ITokenFactory f, String v, int errorCode) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, v, errorCode);
		if (t instanceof TSyntaxError) {
			validCheck(f, v + " ", v + " ", errorCode);		
		} else if (! ((f instanceof TFCommand) || (f instanceof TFLine))) {
			validCheck(f, v + " ", v, errorCode);
		}
	}

	static void validCheck(ITokenFactory f, String v, String compare) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, compare);
	}

	static void validCheck(ITokenFactory f, String v, String compare, int errorCode) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, compare, errorCode);
	}
}
