package com.raygroupintl.parser;

import java.util.List;

import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class DefaultAdapterSupply implements AdapterSupply {
	@Override
	public TString newString() {
		return new TString();
	}
	
	@Override
	public TSequence newSequence(int length) {
		return new TSequence(length);
	}
	
	@Override
	public TList newList() {
		return new TList();
	}
	
	@Override
	public TDelimitedList newDelimitedList(List<Token> tokens) {
		return new TDelimitedList(tokens);
	}

}