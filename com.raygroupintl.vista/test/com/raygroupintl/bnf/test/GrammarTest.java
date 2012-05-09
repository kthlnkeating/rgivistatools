package com.raygroupintl.bnf.test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.annotation.Parser;

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

	@Test
	public void testGrammar() {
		try {
			Token number = grammar.number.tokenize("5.5", 0);
			Assert.assertNotNull(number);
			Assert.assertTrue(number instanceof TNumber);
			Assert.assertEquals("5.5", number.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}		
	}

}
