package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import junit.framework.Assert;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TChar;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TFCommonTest {
	private static void checkObjectType(Token t) {
		if (t instanceof TChar) return;
		if (t instanceof TString) return;
		Assert.assertTrue(t instanceof MToken);
		if (t instanceof TSequence) {
			for (Token r : (TSequence) t) {
				if (r != null) checkObjectType(r);
			}
			return;
		}
		if (t instanceof TList) {
			for (Token r : (TList) t) {
				if (r != null) checkObjectType(r);
			}
			return;			
		}
	}
	
	
	public static void validTokenCheck(Token t, String v) {
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());
		//checkObjectType(t);
	}
	
	static Token validCheck(TokenFactory f, String v, boolean checkWithSpace) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text);
			validTokenCheck(t, v);
			if (checkWithSpace) {
				validCheck(f, v + " ", v);
			}
			return t;
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());	
			return null;
		}
	}

	static Token validCheck(TokenFactory f, String v) {
		return validCheck(f, v, true);
	}

	static Token validCheck(TokenFactory f, String v, Class<? extends Token> cls) {
		Token t = validCheck(f, v, true);
		Assert.assertNotNull(t);
		Assert.assertTrue(t.getClass().equals(cls));
		return t;
	}

	static void nullCheck(TokenFactory f, String v) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text);
			Assert.assertNull(t);
		} catch(SyntaxErrorException e) {
			fail("Unexpected exception.");
		}
	}
	
	static void validCheckNS(TokenFactory f, String v) {
		validCheck(f, v, false);
	}

	static void auxErrorCheck(TokenFactory f, String v, int errorCode, int location) {
		Text text = new Text(v);
		try {
			f.tokenize(text);
			fail("Expected exception did not fire.");
		} catch(SyntaxErrorException e) {
			int actualLocation = text.getIndex();
			Assert.assertEquals(location, actualLocation);
			Assert.assertEquals(errorCode,  e.getCode() == 0 ? MError.ERR_GENERAL_SYNTAX : e.getCode());
		}
	}

	static void errorCheck(TokenFactory f, String v, int errorCode, int location) {
		auxErrorCheck(f, v, errorCode, location);
		if (v.length() > location) {
			auxErrorCheck(f, v + " ", errorCode, location);
		}
	}
	
	static void validCheck(TokenFactory f, String v, String compare) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text);
			validTokenCheck(t, compare);
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}
}
