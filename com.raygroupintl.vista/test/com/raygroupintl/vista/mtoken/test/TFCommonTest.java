package com.raygroupintl.vista.mtoken.test;

import junit.framework.Assert;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFCommonTest {
	static void validCheck(IToken t, String v) {
		Assert.assertFalse(t.hasError());
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());		
	}
		
	static void validCheck(ITokenFactory f, String v) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, v);
	}

	static void validCheck(ITokenFactory f, String v, String compare) {
		IToken t = f.tokenize(v, 0);
		validCheck(t, compare);
	}
}
