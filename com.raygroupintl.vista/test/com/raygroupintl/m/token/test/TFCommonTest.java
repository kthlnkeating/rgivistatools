package com.raygroupintl.m.token.test;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.m.token.TFCommand;
import com.raygroupintl.m.token.TFLine;

public class TFCommonTest {
	private static void validTokenCheck(Token t, String v) {
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());		
	}
	
	static void validCheck(Token t, String v) {
		Assert.assertFalse(t.hasError());
		validTokenCheck(t, v);
	}
			
	static void errorCheck(Token t, String v) {
		Assert.assertTrue(t.hasError());
		validTokenCheck(t, v);
	}
			
	static void errorCheck(Token t, String v, int errorCode) {
		Assert.assertTrue(t.hasError());
		Assert.assertEquals(errorCode,  t.getErrors().get(0).getCode());
		validTokenCheck(t, v);
	}
			
	static void validCheck(TokenFactory f, String v) {
		try {
			Token t = f.tokenize(v, 0);
			validCheck(t, v);
			if (! ((f instanceof TFCommand) || (f instanceof TFLine))) {
				validCheck(f, v + " ", v);
			}
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}

	static void errorCheck(TokenFactory f, String v) {
		try {
			Token t = f.tokenize(v, 0);
			errorCheck(t, v);
		} catch(SyntaxErrorException e) {
			Token t = e.getAsToken(v, 0);
			errorCheck(t, v);
		}
	}

	static void errorCheck(TokenFactory f, String v, int errorCode) {
		try {
			Token t = f.tokenize(v, 0);
			errorCheck(t, v, errorCode);
			if (t instanceof TSyntaxError) {
				errorCheck(f, v + " ", v + " ", errorCode);		
			} else if (! ((f instanceof TFCommand) || (f instanceof TFLine))) {
				errorCheck(f, v + " ", v, errorCode);
			}
		} catch(SyntaxErrorException e) {
			Token t = e.getAsToken(v, 0);
			errorCheck(t, v, errorCode);
			if (t instanceof TSyntaxError) {
				errorCheck(f, v + " ", v + " ", errorCode);		
			} else if (! ((f instanceof TFCommand) || (f instanceof TFLine))) {
				errorCheck(f, v + " ", v, errorCode);
			}
		}
	}

	static void validCheck(TokenFactory f, String v, String compare) {
		try {
			Token t = f.tokenize(v, 0);
			validCheck(t, compare);
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}

	static void errorCheck(TokenFactory f, String v, String compare, int errorCode) {
		try {
			Token t = f.tokenize(v, 0);
			errorCheck(t, compare, errorCode);
		} catch(SyntaxErrorException e) {
			Token t = e.getAsToken(v, 0);
			errorCheck(t, compare, errorCode);
		}
	}
}
