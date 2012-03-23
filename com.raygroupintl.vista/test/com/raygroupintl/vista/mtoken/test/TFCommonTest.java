package com.raygroupintl.vista.mtoken.test;

import junit.framework.Assert;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;

public class TFCommonTest {
	static void validCheck(ITokenFactory f, String v) {
		IToken t = f.tokenize(v, 0);
		Assert.assertFalse(t.hasError());
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());
	}
}
