package com.raygroupintl.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.CharacterAdapter;
import com.raygroupintl.parser.DelimitedListAdapter;
import com.raygroupintl.parser.ListAdapter;
import com.raygroupintl.parser.SequenceAdapter;
import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class DefaultAdapterSupply implements AdapterSupply {
	private CharacterAdapter characterAdapter;
	private StringAdapter stringAdapter;
	private SequenceAdapter sequenceAdapter;
	private ListAdapter listAdapter;
	private DelimitedListAdapter delimitedListAdapter;
	
	private Map<Class<? extends TokenFactory>, Object> adapters;
	
	@Override
	public CharacterAdapter getCharacterAdapter() {
		if (this.characterAdapter == null) {
			this.characterAdapter = new CharacterAdapter() {				
				@Override
				public Token convert(char value) {
					return new TChar(value);
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
					return new TString(value);
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
				public TSequence convert(List<Token> tokens) {
					return new TSequence(tokens);
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
					return new TList(tokens);
				}
			};
		}
		return this.listAdapter;
	}

	@Override
	public DelimitedListAdapter getDelimitedListAdapter() {
		if (this.delimitedListAdapter == null) {
			this.delimitedListAdapter = new DelimitedListAdapter() {				
				@Override
				public Token convert(List<Token> tokens) {
					return new TDelimitedList(tokens);
				}
			};
		}
		return this.delimitedListAdapter;
	}

	private static Map<Class<? extends TokenFactory>, Object> getAdapterMap() {
		Map<Class<? extends TokenFactory>, Object> result = new HashMap<Class<? extends TokenFactory>, Object>();	
		
		result.put(TFCharacter.class, new CharacterAdapter() {			
			@Override
			public Token convert(char value) {
				return new TChar(value);
			}
		});
		
		StringAdapter stringAdapter = new StringAdapter() {				
			@Override
			public Token convert(String value) {
				return new TString(value);
			}
		};		
		result.put(TFString.class, stringAdapter);
		result.put(TFConstant.class, stringAdapter);

		result.put(TFSequence.class, new SequenceAdapter() {			
			@Override
			public TSequence convert(List<Token> tokens) {
				return new TSequence(tokens);
			}
		});
				

		result.put(TFList.class, new ListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				return new TList(tokens);
			}
		});
				
		result.put(TFDelimitedList.class, new DelimitedListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				return new TDelimitedList(tokens);
			}
		});
	
		return result;
	}
	
	@Override
	public Object getAdapter(Class<? extends TokenFactory> tokenCls) {
		if (this.adapters == null) {
			this.adapters = getAdapterMap();
		}
		Object result = this.adapters.get(tokenCls);
		if (result == null) {
			return this.adapters.get(TFSequence.class);
		}
		return result;
	}
}