package com.raygroupintl.bnf.annotation;

import com.raygroupintl.bnf.CharacterAdapter;
import com.raygroupintl.bnf.DelimitedListAdapter;
import com.raygroupintl.bnf.ListAdapter;
import com.raygroupintl.bnf.SequenceAdapter;
import com.raygroupintl.bnf.StringAdapter;

public interface AdapterSupply {
	CharacterAdapter getCharacterAdapter();
	StringAdapter getStringAdapter();
	SequenceAdapter getSequenceAdapter();
	ListAdapter getListAdapter();
	DelimitedListAdapter getDelimitedListAdapter();
}
