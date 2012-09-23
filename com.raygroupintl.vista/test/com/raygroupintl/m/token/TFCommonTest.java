package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import junit.framework.Assert;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parser.annotation.ObjectSupply;

public class TFCommonTest {
	private static void checkObjectType(Token t) {
		Assert.assertTrue(t instanceof MToken);
		if (t instanceof TSequence) {
			for (Token r : (TokenStore) t) {
				if (r != null) checkObjectType(r);
			}
			return;
		}
		if (t instanceof TList) {
			for (Token r : (TokenStore) t) {
				if (r != null) checkObjectType(r);
			}
			return;			
		}
	}
		
	public static void validTokenCheck(Token t, String v) {
		Assert.assertEquals(v, t.toValue().toString());
		Assert.assertEquals(v.length(), t.toValue().length());
		checkObjectType(t);
	}
	
	static Token validCheck(TokenFactory f, ObjectSupply objectSupply, String v, boolean checkWithSpace) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text, objectSupply);
			validTokenCheck(t, v);
			if (checkWithSpace) {
				validCheck(f, objectSupply, v + " ", v);
			}
			return t;
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());	
			return null;
		}
	}

	static Token validCheck(TokenFactory f, ObjectSupply objectSupply, String v) {
		return validCheck(f, objectSupply, v, true);
	}

	static Token validCheck(TokenFactory f, ObjectSupply objectSupply, String v, Class<? extends Token> cls) {
		Token t = validCheck(f, objectSupply, v, true);
		Assert.assertNotNull(t);
		Assert.assertTrue(t.getClass().equals(cls));
		return t;
	}

	static void nullCheck(TokenFactory f, ObjectSupply objectSupply, String v) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text, objectSupply);
			Assert.assertNull(t);
		} catch(SyntaxErrorException e) {
			fail("Unexpected exception.");
		}
	}
	
	static void validCheckNS(TokenFactory f, ObjectSupply objectSupply, String v) {
		validCheck(f, objectSupply, v, false);
	}

	static void auxErrorCheck(TokenFactory f, ObjectSupply objectSupply, String v, int errorCode, int location) {
		Text text = new Text(v);
		try {
			f.tokenize(text, objectSupply);
			fail("Expected exception did not fire.");
		} catch(SyntaxErrorException e) {
			int actualLocation = text.getIndex();
			Assert.assertEquals(location, actualLocation);
			Assert.assertEquals(errorCode,  e.getCode() == 0 ? MError.ERR_GENERAL_SYNTAX : e.getCode());
		}
	}

	static void errorCheck(TokenFactory f, ObjectSupply objectSupply, String v, int errorCode, int location) {
		auxErrorCheck(f, objectSupply, v, errorCode, location);
		if (v.length() > location) {
			auxErrorCheck(f, objectSupply, v + " ", errorCode, location);
		}
	}
	
	static void validCheck(TokenFactory f, ObjectSupply objectSupply, String v, String compare) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text, objectSupply);
			validTokenCheck(t, compare);
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}
}
