package com.raygroupintl.parser.annotation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.RuleGrammar;
import com.raygroupintl.parser.annotation.Parser;
import com.raygroupintl.parser.annotation.TRule;
import com.raygroupintl.parser.annotation.TOptionalSymbols;
import com.raygroupintl.parser.annotation.TRequiredSymbols;
import com.raygroupintl.parser.annotation.TSymbol;

public class RuleGrammarTest {
	private static RuleGrammar spec;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Parser parser = new Parser();
		spec = parser.parse(RuleGrammar.class);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		spec = null;
	}

	@Test
	public void testSymbol() {
		try {
			Text text = new Text("token");
			Token symbol = spec.symbol.tokenize(text);
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
			Text text = new Text("[a, b, ([c], [d])]");
			Token optionalSymbols = spec.optionalsymbols.tokenize(text);
			Assert.assertNotNull(optionalSymbols);
			Assert.assertTrue(optionalSymbols instanceof TOptionalSymbols);
			Assert.assertEquals("[a, b, ([c], [d])]", optionalSymbols.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testRequiredSymbols() {
		try {
			Text text = new Text("([a, b], [c], [d])");
			Token requiredSymbols = spec.requiredsymbols.tokenize(text);
			Assert.assertNotNull(requiredSymbols);
			Assert.assertTrue(requiredSymbols instanceof TRequiredSymbols);
			Assert.assertEquals("([a, b], [c], [d])", requiredSymbols.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testConstSymbols() {
		try {
			Text text = new Text("\"@(\"");
			Token constSymbol = spec.constsymbol.tokenize(text);
			Assert.assertNotNull(constSymbol);
			Assert.assertTrue(constSymbol instanceof TConstSymbol);
			Assert.assertEquals("\"@(\"", constSymbol.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	private void updateMap(Map<String, TokenFactory> map, char ch) {
		Predicate p = new CharPredicate(ch);
		TokenFactory f = new TFCharacter(String.valueOf(ch), p);
		map.put(String.valueOf(ch), f);		
	}
	
	private void testRule(TokenFactory f, String v) {
		try {
			Text text = new Text(v);
			Token result = f.tokenize(text);
			Assert.assertNotNull(result);
			Assert.assertEquals(v, result.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
	private void testRuleError(TokenFactory f, String v, int location) {
		Text text = new Text(v);
		try {
			f.tokenize(text);
			fail("Expected exception did not fire");			
		} catch (SyntaxErrorException se) {
			Assert.assertEquals(location, text.getIndex());
		}
	}

	private Map<String, TokenFactory> getMap() {
		Map<String, TokenFactory> map = new HashMap<String, TokenFactory>();
		char[] chs = {'x', 'y', 'a', 'b', 'c', 'd', 'e'};
		for (char ch : chs) {
			updateMap(map, ch);
		}
		return map;
	}
	
	@Test
	public void testList() {
		try {
			Text text = new Text("x, {y:',':'(':')'}, [{a}, [{b:':'}], [c], d], e");
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("x, {y:',':'(':')'}, [{a}, [{b:':'}], [c], d], e", rule.getStringValue());
			Map<String, TokenFactory> map = this.getMap();
			TokenFactory f = rule.getRule(true).getFactory("test", map);
			Assert.assertNotNull(f);
			Assert.assertTrue(f instanceof TFSequence);
			testRule(f, "x(y)e"); 
			testRule(f, "x(y,y)e"); 
			testRule(f, "x(y,y,y)e"); 
			testRule(f, "x(y)aaab:bde"); 
			testRule(f, "x(y,y)abde"); 
			testRule(f, "x(y,y)abcde"); 
			testRule(f, "x(y,y,y)acde"); 
			testRule(f, "x(y,y,y)aaaade"); 
			testRuleError(f, "xye", 1); 
			testRuleError(f, "x(ya)de", 3); 
			testRuleError(f, "x(yy)abde", 3); 
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testList0() {
		try {
			Text text = new Text("{y:',':'(':')'}, {a}");
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("{y:',':'(':')'}, {a}", rule.getStringValue());
			Map<String, TokenFactory> map = this.getMap();
			TokenFactory f = rule.getRule(true).getFactory("test", map);
			Assert.assertNotNull(f);
			Assert.assertTrue(f instanceof TFSequence);
			testRule(f, "(y)a"); 
			testRule(f, "(y,y)aa"); 
			testRuleError(f, "(y,x)aa", 3); 
			testRuleError(f, "(y,y)", 5); 
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	//@Test
	public void testChar() {
		try {
			Text text = new Text("intlit, ['.', intlit], ['E', ['+' | '-'], intlit]");
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("intlit, ['.', intlit], ['E', ['+' | '-'], intlit]", rule.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testChar0() {
		try {
			Text text = new Text("'+' | '-'");
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("'+' | '-'", rule.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testSequence() {
		try {
			Text text = new Text("x, y, [(a, b), [c], d], e");
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("x, y, [(a, b), [c], d], e", rule.getStringValue());
			Map<String, TokenFactory> map = this.getMap();
			TokenFactory f = rule.getRule(true).getFactory("test", map);
			Assert.assertNotNull(f);
			Assert.assertTrue(f instanceof TFSequence);
			testRule(f, "xye"); 
			testRule(f, "xyabde"); 
			testRule(f, "xyabcde"); 
			testRuleError(f, "xyae", 3); 
			testRuleError(f, "xyde", 2); 
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
	private TokenFactory getFactory(String inputText, Map<String, TokenFactory> map, String name) {
		try {
			Text text = new Text(inputText);
			TRule rule = (TRule) spec.rule.tokenize(text);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals(inputText, rule.getStringValue());
			TokenFactory f = rule.getRule(true).getFactory("namea", map);
			Assert.assertNotNull(f);
			map.put(name, f);
			return f;
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());
			return null;
		}
	}
	
	@Test
	public void testCharSpecified() {
		Map<String, TokenFactory> map = new HashMap<String, TokenFactory>();
		TokenFactory namea = getFactory("{'a' + 'c' + 'd'...'f'}", map, "namea");
		TokenFactory nameb = getFactory("{'a'...'z' - 'd'...'f'}", map, "nameb");
		TokenFactory namec = getFactory("{'a'...'m' + 'q' - 'd'...'f' - 'i'}", map, "namec");
		TokenFactory named = getFactory("{- 'b' + 'a'...'z'}", map, "named");

		testRule(namea, "accdd"); 
		testRule(namea, "efcdd"); 
		testRuleError(namea, "pqr", 0); 
		testRuleError(namea, "abc", 1); 
			
		testRule(nameb, "apqzr"); 
		testRule(nameb, "xyzmn"); 
		testRuleError(nameb, "abcde", 3); 
		testRuleError(nameb, "xfdyz", 1); 
		
		testRule(namec, "amccb"); 
		testRule(namec, "qqacc"); 
		testRuleError(namec, "qqaie", 3); 
		testRuleError(namec, "sertf", 1); 
		
		testRule(named, "accdd"); 
		testRule(named, "efcdd"); 
		testRuleError(named, "bqr", 0); 
		testRuleError(named, "abc", 1); 
	}
}
