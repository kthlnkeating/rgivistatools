package com.raygroupintl.bnf.annotation;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;

public class DescriptionSpecTest {
	private static DescriptionSpec spec;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Parser parser = new Parser();
		spec = parser.parse(DescriptionSpec.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		spec = null;
	}

	@Test
	public void testSymbol() {
		try {
			Token symbol = spec.symbol.tokenize("token", 0);
			Assert.assertNotNull(symbol);
			Assert.assertTrue(symbol instanceof TSymbol);
			Assert.assertEquals("token", symbol.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testOptionalSymbols() {
		try {
			Token optionalSymbols = spec.optionalsymbols.tokenize("[a, b, ([c], [d])]", 0);
			Assert.assertNotNull(optionalSymbols);
			Assert.assertTrue(optionalSymbols instanceof TOptionalSymbols);
			Assert.assertEquals("token", optionalSymbols.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testRequiredSymbols() {
		try {
			Token requiredSymbols = spec.symbol.tokenize("([a, b], [c], [d])", 0);
			Assert.assertNotNull(requiredSymbols);
			Assert.assertTrue(requiredSymbols instanceof TRequiredSymbols);
			Assert.assertEquals("token", requiredSymbols.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testDescription() {
		try {
			Token description = spec.description.tokenize("x, y, [(a, b), [c], d], e", 0);
			Assert.assertNotNull(description);
			Assert.assertTrue(description instanceof TDescription);
			Assert.assertEquals("token", description.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
}
