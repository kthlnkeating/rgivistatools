package com.raygroupintl.parser.annotation;

import java.util.List;

import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;

public interface AdapterSupply {
	StringAdapter getStringAdapter();
	
	Object getAdapter(Class<? extends TokenFactory> tokenCls);
	
	TSequence newSequence(int length);
	TList newList();
	TDelimitedList newDelimitedList(List<Token> tokens);
}
