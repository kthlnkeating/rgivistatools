package com.raygroupintl.bnf.annotation;

import java.util.Map;

import com.raygroupintl.bnf.DefaultCharacterAdapter;
import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.TFCharacter;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.charlib.CharPredicate;

public class TCharSymbol extends TSequence implements SequencePieceGenerator {
	public TCharSymbol(java.util.List<Token> tokens) {
		super(tokens);
	}
	
	@Override
	public TokenFactory getFactory(String name, Map<String, TokenFactory> map) {
		String value = this.get(1).getStringValue();
		char ch = value.charAt(0);
		String sch = String.valueOf(ch);
		TFCharacter result = new TFCharacter(sch, new CharPredicate(ch), new DefaultCharacterAdapter());
		return result;
	}
	
	@Override	
	public boolean getRequired() {
		return true;
	}	
}
