package com.raygroupintl.parser.annotation;

import static org.junit.Assert.*;

import java.util.HashMap;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parser.DefaultObjectSupply;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ObjectSupply;
import com.raygroupintl.parsergen.rulebased.FSRCustom;
import com.raygroupintl.parsergen.rulebased.FactorySupplyRule;
import com.raygroupintl.parsergen.rulebased.RulesMapByName;
import com.raygroupintl.parsergen.ruledef.RuleDefinitionParserGenerator;
import com.raygroupintl.parsergen.ruledef.RuleGrammar;
import com.raygroupintl.parsergen.ruledef.TConstSymbol;
import com.raygroupintl.parsergen.ruledef.TOptionalSymbols;
import com.raygroupintl.parsergen.ruledef.TRequiredSymbols;
import com.raygroupintl.parsergen.ruledef.TRule;
import com.raygroupintl.parsergen.ruledef.TSymbol;

public class RuleGrammarTest {
	private static RuleGrammar spec;
	private static ObjectSupply objectSupply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		RuleDefinitionParserGenerator parserGen = new RuleDefinitionParserGenerator();
		spec = parserGen.generate(RuleGrammar.class);
		objectSupply = new DefaultObjectSupply();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		spec = null;
		objectSupply = null;
	}

	@Test
	public void testSymbol() {
		try {
			Text text = new Text("token");
			Token symbol = spec.symbol.tokenize(text, objectSupply);
			Assert.assertNotNull(symbol);
			Assert.assertTrue(symbol instanceof TSymbol);
			Assert.assertEquals("token", symbol.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testOptionalSymbols() {
		try {
			Text text = new Text("[a, b, ([c], [d])]");
			Token optionalSymbols = spec.optionalsymbols.tokenize(text, objectSupply);
			Assert.assertNotNull(optionalSymbols);
			Assert.assertTrue(optionalSymbols instanceof TOptionalSymbols);
			Assert.assertEquals("[a, b, ([c], [d])]", optionalSymbols.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testRequiredSymbols() {
		try {
			Text text = new Text("([a, b], [c], [d])");
			Token requiredSymbols = spec.requiredsymbols.tokenize(text, objectSupply);
			Assert.assertNotNull(requiredSymbols);
			Assert.assertTrue(requiredSymbols instanceof TRequiredSymbols);
			Assert.assertEquals("([a, b], [c], [d])", requiredSymbols.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testConstSymbols() {
		try {
			Text text = new Text("\"@(\"");
			Token constSymbol = spec.constsymbol.tokenize(text, objectSupply);
			Assert.assertNotNull(constSymbol);
			Assert.assertTrue(constSymbol instanceof TConstSymbol);
			Assert.assertEquals("\"@(\"", constSymbol.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	private void updateMap(RulesMapByName map, char ch) {
		Predicate p = new CharPredicate(ch);
		TokenFactory f = new TFCharacter(String.valueOf(ch), p);
		FSRCustom r = new FSRCustom(f);
		map.put(String.valueOf(ch), r);		
	}
	
	private void testRule(TokenFactory f, String v, String compare) {
		try {
			Text text = new Text(v);
			Token result = f.tokenize(text, objectSupply);
			Assert.assertNotNull(result);
			Assert.assertEquals(compare, result.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
	private void testRule(TokenFactory f, String v) {
		testRule(f, v, v);
	}
	
	private void testRuleError(TokenFactory f, String v, int location) {
		Text text = new Text(v);
		try {
			f.tokenize(text, objectSupply);
			fail("Expected exception did not fire");			
		} catch (SyntaxErrorException se) {
			Assert.assertEquals(location, text.getIndex());
		}
	}

	private void testRuleNull(TokenFactory f, String v) {
		try {
			Text text = new Text(v);
			Token result = f.tokenize(text, objectSupply);
			Assert.assertNull(result);
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}
	
	private RulesMapByName getMap() {
		RulesMapByName map = new RulesMapByName(new HashMap<String, FactorySupplyRule>());
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
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("x, {y:',':'(':')'}, [{a}, [{b:':'}], [c], d], e", rule.toValue().toString());
			RulesMapByName map = this.getMap();
			FactorySupplyRule r = rule.getRule("test");
			r.update(map);
			TokenFactory f = r.getTheFactory(map);
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
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("{y:',':'(':')'}, {a}", rule.toValue().toString());
			RulesMapByName map = this.getMap();
			FactorySupplyRule r = rule.getRule("test");
			r.update(map);
			TokenFactory f = r.getTheFactory(map);
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
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("intlit, ['.', intlit], ['E', ['+' | '-'], intlit]", rule.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testChar0() {
		try {
			Text text = new Text("'+' | '-'");
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("'+' | '-'", rule.toValue().toString());
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
	}

	@Test
	public void testSequence() {
		try {
			Text text = new Text("x, y, [(a, b), [c], d], e");
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals("x, y, [(a, b), [c], d], e", rule.toValue().toString());
			RulesMapByName map = this.getMap();
			FactorySupplyRule r = rule.getRule("test");
			r.update(map);
			TokenFactory f = r.getTheFactory(map);
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
	
	private TokenFactory getFactory(String inputText, RulesMapByName map, String name) {
		try {
			Text text = new Text(inputText);
			TRule rule = (TRule) spec.rule.tokenize(text, objectSupply);
			Assert.assertNotNull(rule);
			Assert.assertTrue(rule instanceof TRule);
			Assert.assertEquals(inputText, rule.toValue().toString());
			FactorySupplyRule r =  rule.getRule(name);
			r.update(map);
			TokenFactory f = r.getTheFactory(map);
			Assert.assertNotNull(f);
			map.put(name, r);
			return f;
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());
			return null;
		}
	}
	
	@Test
	public void testCharSpecified() {
		RulesMapByName map = new RulesMapByName(new HashMap<String, FactorySupplyRule>());
		TokenFactory namea = getFactory("{'a' + 'c' + 'd'...'f'}", map, "namea");
		TokenFactory nameb = getFactory("{'a'...'z' - 'd'...'f'}", map, "nameb");
		TokenFactory namec = getFactory("{'a'...'m' + 'q' - 'd'...'f' - 'i'}", map, "namec");
		TokenFactory named = getFactory("{- 'b' + 'a'...'z'}", map, "named");
		TokenFactory namee = getFactory("'%' + 'a'...'z' + 'A'...'Z', [{'a'...'z' + 'A'...'Z' + '0'...'9'}]", map, "namee");
		
		testRule(namea, "accdd"); 
		testRule(namea, "efcdd"); 
		testRuleNull(namea, "pqr"); 
		testRule(namea, "abc", "a"); 
			
		testRule(nameb, "apqzr"); 
		testRule(nameb, "xyzmn"); 
		testRule(nameb, "abcde", "abc"); 
		testRule(nameb, "xfdyz", "x"); 
		
		testRule(namec, "amccb"); 
		testRule(namec, "qqacc"); 
		testRule(namec, "qqaie", "qqa"); 
		testRule(namec, "gertf", "g"); 
		
		testRule(named, "accdd"); 
		testRule(named, "efcdd"); 
		testRuleNull(named, "bqr"); 
		testRule(named, "afbc", "af"); 

		testRule(namee, "TAG1"); 
		testRule(namee, "%"); 
		testRule(namee, "tAt1"); 
		testRule(namee, "%tag"); 
		testRuleNull(namee, "1bqr"); 
		testRule(namee, "af+bc", "af"); 
	}
	
	@Test
	public void testListInChoice() {
		RulesMapByName map = new RulesMapByName(new HashMap<String, FactorySupplyRule>());
		TokenFactory name = getFactory("{'a'...'z'}", map, "name");
		TokenFactory number = getFactory("{'0'...'9'}", map, "number");
		TokenFactory rule = getFactory("{name:',':'(':')'} | number", map, "rule");
		
		testRule(name, "rgi");
		testRule(number, "232");
		testRule(rule, "(rgi,dc)");
		testRule(rule, "1235");
		testRuleNull(rule, "RGI"); 
		testRuleError(rule, "(rgi,A)", 5);
	}	
}
