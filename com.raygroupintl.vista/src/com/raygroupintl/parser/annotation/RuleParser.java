package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.DefaultObjectSupply;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;

public class RuleParser {
	private RuleGrammar grammar;

	public RuleSupply getTopTFRule(String name, String ruleText) {
		if (this.grammar == null) {
			try {
				Parser parser = new Parser();
				this.grammar = parser.parse(RuleGrammar.class, true);
			} catch (ParseException e) {
				throw new ParseErrorException("Error in rule grammar: " + e.getMessage());
			}
		}
		Text text = new Text(ruleText);
		try {
			ObjectSupply objectSupply = new DefaultObjectSupply();
			TRule t = (TRule) this.grammar.rule.tokenize(text, objectSupply);
			int tLength = t.toValue().length();
			if (tLength != ruleText.length()) {
				String msg = "Error in rule " + name + " at position " + String.valueOf(tLength);		
				throw new ParseErrorException(msg);					
			}
			return t;
		} catch (SyntaxErrorException e) {
			int errorLocation = text.getIndex();
			String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
			throw new ParseErrorException(msg);
		}		
	}	
}
