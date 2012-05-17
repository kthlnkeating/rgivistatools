package com.raygroupintl.parser.annotation;

import com.raygroupintl.parser.CharacterAdapter;
import com.raygroupintl.parser.DelimitedListAdapter;
import com.raygroupintl.parser.ListAdapter;
import com.raygroupintl.parser.SequenceAdapter;
import com.raygroupintl.parser.StringAdapter;

public interface AdapterSupply {
	CharacterAdapter getCharacterAdapter();
	StringAdapter getStringAdapter();
	SequenceAdapter getSequenceAdapter();
	ListAdapter getListAdapter();
	DelimitedListAdapter getDelimitedListAdapter();
}
