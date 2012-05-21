//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.parser;

import java.lang.reflect.Constructor;
import java.util.List;

public final class TFList extends TFBasic {
	private static final ListAdapter DEFAULT_ADAPTER = new ListAdapter() {		
		@Override
		public Token convert(List<Token> tokens) {
			return new TList(tokens);
		}
	}; 
	
	private TokenFactory elementFactory;
	private ListAdapter adapter;
	
	public TFList(String name) {
		this(name, DEFAULT_ADAPTER);
	}
	
	public TFList(String name, ListAdapter adapter) {
		super(name);
		this.adapter = adapter == null ? DEFAULT_ADAPTER : adapter;
	}
	
	public TFList(String name, TokenFactory elementFactory) {
		this(name, DEFAULT_ADAPTER, elementFactory);
	}
	
	public TFList(String name, ListAdapter adapter, TokenFactory elementFactory) {
		super(name);
		this.adapter = adapter == null ? DEFAULT_ADAPTER : adapter;
		this.elementFactory = elementFactory;
	}
	
	@Override
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFList) {
			TFList rhsCasted = (TFList) rhs;
			this.elementFactory = rhsCasted.elementFactory;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFList.class.getName());
		}
	}

	@Override
	public TFBasic getCopy(String name) {
		return new TFList(name, this.adapter, this.elementFactory);
	}

	public void setAdapter(ListAdapter adapter) {
		this.adapter = adapter;
	}
	
	public void setElement(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	private Token getToken(List<Token> list) {
		return this.adapter.convert(list);
	}

	@Override
	public Token tokenize(Text text) throws SyntaxErrorException {
		if (elementFactory == null) throw new IllegalStateException("TFList.setElementFactory needs to be called before TFList.tokenize");
		
		if (text.onChar()) {
			ListAsTokenStore list = new ListAsTokenStore();
			while (text.onChar()) {
				Token token = null;
				try {
					token = this.elementFactory.tokenize(text);
				} catch (SyntaxErrorException e) {
					throw e;
				}
				if (token == null) {
					if (list.hasToken()) {
						return this.getToken(list.toList());
					} else {
						return null;
					}
				}
				list.addToken(token);	
			}
			return this.getToken(list.toList());
		}
		return null;
	}
	
	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = this.getConstructor(cls, List.class, TList.class);
		this.adapter = new ListAdapter() {			
			@Override
			public Token convert(List<Token> tokens) {
				try{
					return (Token) constructor.newInstance(tokens);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
	
	@Override
	public void setAdapter(Object adapter) {
		if (adapter instanceof ListAdapter) {
			this.adapter = (ListAdapter) adapter;					
		} else {
			throw new IllegalArgumentException("Wrong adapter type " + adapter.getClass().getName());
		}
	}	
}
