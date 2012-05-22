package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;

public class RuleParser {
	private RuleGrammar grammar;

	public TopTFRule getTopTFRule(String name, String ruleText, Map<String, TopTFRule> existing) {
		if (this.grammar == null) {
			try {
				Parser parser = new Parser();
				this.grammar = parser.parse(RuleGrammar.class, null, true);
			} catch (ParseException e) {
				throw new ParseErrorException("Error in rule grammar.");
			}
		}
		Text text = new Text(ruleText);
		try {
			TRule t = (TRule) this.grammar.rule.tokenize(text);
			if (t.getStringSize() != ruleText.length()) {
				int errorLocation = t.getStringSize();
				String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
				throw new ParseErrorException(msg);					
			}
			return t.getTopTFRule(name, existing);
		} catch (SyntaxErrorException e) {
			int errorLocation = text.getIndex();
			String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
			throw new ParseErrorException(msg);
		}		
	}	
}
