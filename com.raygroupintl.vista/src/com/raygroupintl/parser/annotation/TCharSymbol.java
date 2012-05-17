package com.raygroupintl.parser.annotation;

import java.util.Map;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public class TCharSymbol extends TSequence implements SequencePieceGenerator {
	public TCharSymbol(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		String value = this.get(1).getStringValue();
		char ch = value.charAt(0);
		String sch = String.valueOf(ch);
		TFCharacter result = new TFCharacter(sch, new CharPredicate(ch));
		return result;
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
