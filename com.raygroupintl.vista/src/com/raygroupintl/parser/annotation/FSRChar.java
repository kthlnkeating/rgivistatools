package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TokenFactory;

public class FSRChar extends FSRBase {
	private char ch;
	
	public FSRChar(char ch, boolean required) {
		super(required);
		this.ch = ch;
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> symbols) {
		String key = "'" + this.ch + "'";
		TokenFactory result = symbols.get(key);
		if (result == null) {		
			result = new TFCharacter(key, new CharPredicate(this.ch));
			symbols.put(key, result);
		}
		return result;
	}
}
