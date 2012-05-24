package com.raygroupintl.parser;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.annotation.AdapterSupply;
import com.raygroupintl.parser.annotation.Parser;

public class GrammarTest {
	private static Grammar grammar;
	private static AdapterSupply adapterSupply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Parser parser = new Parser();
		grammar = parser.parse(Grammar.class);
		adapterSupply = new DefaultAdapterSupply();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		grammar = null;
		adapterSupply = null;
	}

	private void testCommon(String v, Token t) {
		Assert.assertNotNull(t);
		Assert.assertEquals(v, t.getStringValue());
	}
	
	private void testCommonNumber(String v) {
		try {
			Text text = new Text(v, 0);
			Token number = grammar.number.tokenize(text, adapterSupply);
			Assert.assertTrue(number instanceof TNumber);
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
	
	@Test
	public void testNumber() {
		testCommonNumber("5.5");
		testCommonNumber("1.0E-7");
		testCommonNumber(".5E+7");
		testCommonNumber(".5");
	}

	@Test
	public void testPipe() {
		try {
			String v = "a+b.r";
			Text text = new Text(v);
			Token expr = grammar.expr.tokenize(text, adapterSupply);
			testCommon(v, expr);
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
}
