package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.DelimitedListAdapter;
import com.raygroupintl.parser.ListAdapter;
import com.raygroupintl.parser.SequenceAdapter;
import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFList;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class MAdapterSupply implements AdapterSupply {
	private StringAdapter stringAdapter;
	private SequenceAdapter sequenceAdapter;
	private ListAdapter listAdapter;
	private DelimitedListAdapter delimitedListAdapter;
	
	private Map<Class<? extends TokenFactory>, Object> adapters;
	
	@Override
	public StringAdapter getStringAdapter() {
		if (this.stringAdapter == null) {
			this.stringAdapter = new StringAdapter() {				
				@Override
				public Token convert(StringPiece value) {
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
				public TSequence convert(Token token) {
					return new MTSequence(token);
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
		return this.listAdapter;
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

	private static Map<Class<? extends TokenFactory>, Object> getAdapterMap() {
		Map<Class<? extends TokenFactory>, Object> result = new HashMap<Class<? extends TokenFactory>, Object>();	
		
		StringAdapter stringAdapter = new StringAdapter() {				
			@Override
			public Token convert(StringPiece value) {
				return new MTString(value);
			}
		};		
		result.put(TFString.class, stringAdapter);
		result.put(TFConstant.class, stringAdapter);

		result.put(TFSequence.class, new SequenceAdapter() {			
			@Override
			public TSequence convert(Token token) {
				return new MTSequence(token);
			}
		});
				

		result.put(TFList.class, new ListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				return new MTList(tokens);
			}
		});
				
		result.put(TFDelimitedList.class, new DelimitedListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				return new MTDelimitedList(tokens);
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
	
	public MTSequence newSequence(int length) {
		return new MTSequence(length);
	}
	
	public MTList newList() {
		return new MTList();
	}
	
	public TDelimitedList newDelimitedList() {
		return new MTDelimitedList();
	}
}
