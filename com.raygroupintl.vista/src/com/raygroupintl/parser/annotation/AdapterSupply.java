package com.raygroupintl.parser.annotation;

import java.util.List;

import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public interface AdapterSupply {
	public TString newString();
	TSequence newSequence(int length);
	TList newList();
	TDelimitedList newDelimitedList(List<Token> tokens);
}
