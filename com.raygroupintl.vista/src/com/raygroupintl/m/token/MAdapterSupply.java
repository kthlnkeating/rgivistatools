package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.CharacterAdapter;
import com.raygroupintl.bnf.DelimitedListAdapter;
import com.raygroupintl.bnf.ListAdapter;
import com.raygroupintl.bnf.SequenceAdapter;
import com.raygroupintl.bnf.StringAdapter;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.annotation.AdapterSupply;

public class MAdapterSupply implements AdapterSupply {
	private CharacterAdapter characterAdapter;
	private StringAdapter stringAdapter;
	private SequenceAdapter sequenceAdapter;
	private ListAdapter listAdapter;
	private DelimitedListAdapter delimitedListAdapter;
		
	@Override
	public CharacterAdapter getCharacterAdapter() {
		if (this.characterAdapter == null) {
			this.characterAdapter = new CharacterAdapter() {				
				@Override
				public Token convert(char value) {
					return new MTChar(value);
				}
			};
		}		
		return characterAdapter;
	}

	@Override
	public StringAdapter getStringAdapter() {
		if (this.stringAdapter == null) {
			this.stringAdapter = new StringAdapter() {				
				@Override
				public Token convert(String value) {
					return new MTString(value);
				}
			};
		}
		return this.stringAdapter;
	}

	@Override
	public SequenceAdapter getSequenceAdapter() {
		if (this.sequenceAdapter == null) {
			this.sequenceAdapter = new SequenceAdapter() {				
				@Override
				public Token convert(List<Token> tokens) {
					return new MTSequence(tokens);
				}
			};
		}
		return this.sequenceAdapter;
	}

	@Override
	public ListAdapter getListAdapter() {
		if (this.listAdapter == null) {
			this.listAdapter = new ListAdapter() {				
				@Override
				public Token convert(List<Token> tokens) {
					return new MTList(tokens);
				}
			};
		}
		return null;
	}

	@Override
	public DelimitedListAdapter getDelimitedListAdapter() {
		if (this.delimitedListAdapter == null) {
			this.delimitedListAdapter = new DelimitedListAdapter() {				
				@Override
				public Token convert(List<Token> tokens) {
					return new MTDelimitedList(tokens);
				}
			};
		}
		return this.delimitedListAdapter;
	}
}
