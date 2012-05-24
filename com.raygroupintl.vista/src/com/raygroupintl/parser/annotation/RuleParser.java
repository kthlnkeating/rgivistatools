package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.DefaultAdapterSupply;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;

public class RuleParser {
	private RuleGrammar grammar;

	public TopTFRule getTopTFRule(String name, String ruleText) {
		if (this.grammar == null) {
			try {
				Parser parser = new Parser();
				this.grammar = parser.parse(RuleGrammar.class, true);
			} catch (ParseException e) {
				throw new ParseErrorException("Error in rule grammar.");
			}
		}
		Text text = new Text(ruleText);
		try {
			AdapterSupply adapterSupply = new DefaultAdapterSupply();
			TRule t = (TRule) this.grammar.rule.tokenize(text, adapterSupply);
			if (t.getStringSize() != ruleText.length()) {
				int errorLocation = t.getStringSize();
				String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
				throw new ParseErrorException(msg);					
			}
			return (TopTFRule) t.getRule(true);
		} catch (SyntaxErrorException e) {
			int errorLocation = text.getIndex();
			String msg = "Error in rule " + name + " at position " + String.valueOf(errorLocation);		
			throw new ParseErrorException(msg);
		}		
	}	
}
