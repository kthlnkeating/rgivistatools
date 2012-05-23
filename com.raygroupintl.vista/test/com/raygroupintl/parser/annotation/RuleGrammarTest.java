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
import com.raygroupintl.parser.TSequence;
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
	
	private void testTDescription(TokenFactory f, String v) {
		try {
			Text text = new Text(v);
			Token result = f.tokenize(text);
			Assert.assertNotNull(result);
			Assert.assertTrue(result instanceof TSequence);
			Assert.assertEquals(v, result.getStringValue());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
	private void testErrorTDescription(TokenFactory f, String v) {
		try {
			Text text = new Text(v);
			f.tokenize(text);
			fail("Expected exception did not fire");			
		} catch (SyntaxErrorException se) {
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
			testTDescription(f, "x(y)e"); 
			testTDescription(f, "x(y,y)e"); 
			testTDescription(f, "x(y,y,y)e"); 
			testTDescription(f, "x(y)aaab:bde"); 
			testTDescription(f, "x(y,y)abde"); 
			testTDescription(f, "x(y,y)abcde"); 
			testTDescription(f, "x(y,y,y)acde"); 
			testTDescription(f, "x(y,y,y)aaaade"); 
			testErrorTDescription(f, "xye"); 
			testErrorTDescription(f, "x(ya)de"); 
			testErrorTDescription(f, "x(yy)abde"); 
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
			testTDescription(f, "(y)a"); 
			testTDescription(f, "(y,y)aa"); 
			testErrorTDescription(f, "(y,x)aa"); 
			testErrorTDescription(f, "(y,y)"); 
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
			testTDescription(f, "xye"); 
			testTDescription(f, "xyabde"); 
			testTDescription(f, "xyabcde"); 
			testErrorTDescription(f, "xyae"); 
			testErrorTDescription(f, "xyde"); 
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
}
