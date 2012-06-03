package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class MAdapterSupply implements AdapterSupply {
	@Override
	public MTString newString() {
		return new MTString();
	}
	
	@Override
	public MTSequence newSequence(int length) {
		return new MTSequence(length);
	}
	
	@Override
	public MTList newList() {
		return new MTList();
	}
	
	@Override
	public TDelimitedList newDelimitedList(List<Token> tokens) {
		return new MTDelimitedList(tokens);
	}
}
