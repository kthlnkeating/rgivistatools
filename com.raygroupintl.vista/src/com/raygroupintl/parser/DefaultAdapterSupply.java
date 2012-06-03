package com.raygroupintl.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.TFConstant;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.AdapterSupply;

public class DefaultAdapterSupply implements AdapterSupply {
	private StringAdapter stringAdapter;
	
	private Map<Class<? extends TokenFactory>, Object> adapters;
	
	@Override
	public StringAdapter getStringAdapter() {
		if (this.stringAdapter == null) {
			this.stringAdapter = new StringAdapter() {				
				@Override
				public Token convert(StringPiece value) {
					return new TString(value);
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
				return new TString(value);
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