package com.raygroupintl.parsergen.ruledef;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.parsergen.ParseException;
import com.raygroupintl.parsergen.rulebased.RuleBasedParserGenerator;

public class GrammarErrorTest {
	private static RuleBasedParserGenerator parserGen;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parserGen = new RuleBasedParserGenerator();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		parserGen = null;
	}

	@Test
	public void testGrammarInError() {
		try {
			parserGen.generate(GrammarInError0.class);
		} catch (ParseException pe) {
			return;
		} catch (Throwable t) {
			fail("Wrong exception: " + t.getClass().getName());			
		}
		fail("Expected exception did not fire");			
	}
}
