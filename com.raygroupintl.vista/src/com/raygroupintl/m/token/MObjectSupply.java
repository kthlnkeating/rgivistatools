package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TEmpty;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.annotation.ObjectSupply;

public class MObjectSupply implements ObjectSupply {
	@Override
	public MString newString() {
		return new MString();
	}
	
	@Override
	public MSequence newSequence(int length) {
		return new MSequence(length);
	}
	
	@Override
	public MList newList() {
		return new MList();
	}
	
	@Override
	public TDelimitedList newDelimitedList(List<Token> tokens) {
		return new MDelimitedList(tokens);
	}
	
	@Override
	public TEmpty newEmpty() {
		return new MEmpty();
	}
}
