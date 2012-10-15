package com.raygroupintl.parsergen.ruledef;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ObjectSupply;
import com.raygroupintl.parsergen.rulebased.RuleBasedParserGenerator;
import com.raygroupintl.parsergen.ruledef.DefaultObjectSupply;
import com.raygroupintl.parsergen.ruledef.TString;

public class GrammarTest {
	private static Grammar grammar;
	private static ObjectSupply objectSupply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		RuleBasedParserGenerator parserGen = new RuleBasedParserGenerator();
		grammar = parserGen.generate(Grammar.class);
		objectSupply = new DefaultObjectSupply();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		grammar = null;
		objectSupply = null;
	}

	private void testCommon(String v, Token t) {
		Assert.assertNotNull(t);
		Assert.assertEquals(v, t.toValue().toString());
	}
	
	private void testCommonNumber(String v) {
		try {
			Text text = new Text(v, 0);
			Token number = grammar.number.tokenize(text, objectSupply);
			Assert.assertTrue(number instanceof TNumber);
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
	
	@Test
	public void testNumber() {
		testCommonNumber("1");
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
			Token expr = grammar.expr.tokenize(text, objectSupply);
			testCommon(v, expr);
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
	
	public void testChoice(TokenFactory f, String v, Class<?> cls, int seqIndex) {
		try {
			Text text = new Text(v, 0);
			Token t = f.tokenize(text, objectSupply);
			this.testCommon(v, t);
			if (seqIndex < 0) {
				Assert.assertTrue(t.getClass().equals(cls));
			} else {
				Token tseq = ((TokenStore) t).get(seqIndex);
				Assert.assertTrue(tseq.getClass().equals(cls));
			}
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}				
	}
	
	public void testChoiceError(TokenFactory f, String v) {
		try {
			Text text = new Text(v, 0);
			f.tokenize(text, objectSupply);
			fail("Expected exception did not fire");			
		} catch (SyntaxErrorException se) {
		}		
	}		
	
	@Test
	public void testChoice() {
		testChoice(grammar.testchoicea, "1", TNumber.class, -1);
		testChoice(grammar.testchoicea, "a^a", TNameA.class, 0);
		testChoice(grammar.testchoicea, "a:a", TNameB.class, 0);
		testChoiceError(grammar.testchoicea, "a");
		
		testChoice(grammar.testchoiceb, "1", TNumber.class, -1);
		testChoice(grammar.testchoiceb, "a^a", TNameA.class, 0);
		testChoice(grammar.testchoiceb, "a:a", TNameB.class, 0);
		testChoice(grammar.testchoiceb, "a", TString.class, -1);
		testChoiceError(grammar.testchoicea, "a1");

		testChoice(grammar.testchoicec, "1", TNumber.class, -1);
		testChoice(grammar.testchoicec, "a1", TString.class, 0);
		testChoice(grammar.testchoicec, "a^a", TNameA.class, 0);
		testChoice(grammar.testchoicec, "a", TString.class, -1);

		testChoice(grammar.testchoiced, "1", TNumber.class, -1);
		testChoice(grammar.testchoiced, "a1", TString.class, 0);
		testChoice(grammar.testchoiced, "a^a", TNameA.class, 0);
		testChoice(grammar.testchoiced, "a", TNameB.class, -1);
	}
}
