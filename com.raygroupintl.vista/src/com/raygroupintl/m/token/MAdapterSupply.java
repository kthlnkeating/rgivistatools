package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class MAdapterSupply implements AdapterSupply {
	private StringAdapter stringAdapter;
	
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
	
	public TDelimitedList newDelimitedList(List<Token> tokens) {
		return new MTDelimitedList(tokens);
	}
}
