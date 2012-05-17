package com.raygroupintl.parser;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.annotation.Parser;

public class GrammarTest {
	private static Grammar grammar;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Parser parser = new Parser();
		grammar = parser.parse(Grammar.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		grammar = null;
	}

	private void testCommon(String v) {
		try {
			Text text = new Text(v, 0);
			Token number = grammar.number.tokenize(text);
			Assert.assertNotNull(number);
			Assert.assertTrue(number instanceof TNumber);
			Assert.assertEquals(v, number.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
	
	private void testErrorCommon(String v) {
		try {
			Text text = new Text(v, 0);
			Token number = grammar.number.tokenize(text);
			Assert.assertNotNull(number);
			Assert.assertTrue(number instanceof TNumber);
			Assert.assertFalse(v.equals(number.getStringValue()));
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}

	@Test
	public void testGrammar() {
		testCommon("5.5");
		testCommon("1.0E-7");
		testCommon(".5E+7");
		testErrorCommon(".5X+7");
	}

}
