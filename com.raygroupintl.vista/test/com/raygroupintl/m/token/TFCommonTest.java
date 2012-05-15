package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import junit.framework.Assert;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Text;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TokenStore;
import com.raygroupintl.m.token.TSyntaxError;
import com.raygroupintl.vista.struct.MError;

public class TFCommonTest {
	public static void validTokenCheck(Token t, String v) {
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());		
	}
	
	public static Token getErrorToken(SyntaxErrorException e, int location, String v) {
		Token t = new TSyntaxError(0, v.substring(location), location);
		for (TokenStore ts : e.getTokenStores()) {
			ts.addToken(t);
			t = ts.toToken();
		}
		return t;
	}
	
	static void errorCheck(SyntaxErrorException e, int location, String v) {
		Token t = getErrorToken(e, location, v);
		validTokenCheck(t, v);
	}
			
	static void errorCheck(SyntaxErrorException e, int location, String v, int errorCode) {
		Assert.assertEquals(errorCode,  e.getCode() == 0 ? MError.ERR_GENERAL_SYNTAX : e.getCode());
		Token t = getErrorToken(e, location, v);
		validTokenCheck(t, v);
	}
			
	static void validCheck(TokenFactory f, String v, boolean checkWithSpace) {
		try {
			Text text = new Text(v);
			Token t = f.tokenize(text);
			validTokenCheck(t, v);
			if (checkWithSpace) {
				validCheck(f, v + " ", v);
			}
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}

	static void validCheck(TokenFactory f, String v) {
		validCheck(f, v, true);
	}

	static void nullCheck(TokenFactory f, String v) {
		try {
			Token t = f.tokenize(v, 0);
			Assert.assertNull(t);
		} catch(SyntaxErrorException e) {
			fail("Unexpected exception.");
		}
	}
	
	static void validCheckNS(TokenFactory f, String v) {
		validCheck(f, v, false);
	}

	static void errorCheck(TokenFactory f, String v) {
		Text text = new Text(v);
		try {
			f.tokenize(text);
			fail("Expected exception did not fire.");
		} catch(SyntaxErrorException e) {
			errorCheck(e, text.getIndex(), v);
		}
	}

	static void errorCheck(TokenFactory f, String v, int errorCode, boolean checkWithSpace) {
		Text text = new Text(v);
		try {
			f.tokenize(text);
			fail("Expected exception did not fire.");
		} catch(SyntaxErrorException e) {
			errorCheck(e, text.getIndex(), v, errorCode);
			errorCheck(f, v + " ", v + " ", errorCode);		
			if (checkWithSpace) {
				errorCheck(f, v + " ", v, errorCode);
			}
		}
	}

	static void errorCheck(TokenFactory f, String v, int errorCode) {
		errorCheck(f, v, errorCode, true);
	}

	static void errorCheckNS(TokenFactory f, String v, int errorCode) {
		errorCheck(f, v, errorCode, false);
	}

	static void validCheck(TokenFactory f, String v, String compare) {
		try {
			Token t = f.tokenize(v, 0);
			validTokenCheck(t, compare);
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());			
		}
	}

	static void errorCheck(TokenFactory f, String v, String compare, int errorCode) {
		Text text = new Text(v);
		try {
			f.tokenize(text);
			fail("Expected exception did not fire.");
		} catch(SyntaxErrorException e) {
			errorCheck(e, text.getIndex(), compare, errorCode);
		}
	}
}
