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

import com.raygroupintl.parser.annotation.AdapterSupply;

public final class TFList extends TFBasic {
	private TokenFactory elementFactory;
	private ListAdapter adapter;
	
	public TFList(String name) {
		super(name);
	}
	
	public TFList(String name, TokenFactory elementFactory) {
		super(name);
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
		return new TFList(name, this.elementFactory);
	}

	@Override
	public boolean isInitialized() {
		return this.elementFactory != null;
	}

	public void setElement(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	private Token getToken(List<Token> list, AdapterSupply adapterSupply) {
		return this.adapter == null ? adapterSupply.getListAdapter().convert(list) : this.adapter.convert(list);
	}

	public List<Token> tokenizeCommon(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (elementFactory == null) throw new IllegalStateException("TFList.setElementFactory needs to be called before TFList.tokenize");
		
		if (text.onChar()) {
			ListAsTokenStore list = new ListAsTokenStore();
			while (text.onChar()) {
				Token token = null;
				try {
					token = this.elementFactory.tokenize(text, adapterSupply);
				} catch (SyntaxErrorException e) {
					throw e;
				}
				if (token == null) {
					if (list.hasToken()) {
						return list.toList();
					} else {
						return null;
					}
				}
				list.addToken(token);	
			}
			return list.toList();
		}
		return null;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		List<Token> rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			return this.getToken(rawResult, adapterSupply);	
		}
	}

	@Override
	public Token tokenizeRaw(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		List<Token> rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			return adapterSupply.getListAdapter().convert(rawResult);	
		}
	}

	@Override
	protected Token convert(Token token) {
		if ((this.adapter != null) && (token instanceof TList)) {
			return this.adapter.convert(((TList) token).getList()); 
		} else {
			return token;
		}
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, List.class, TList.class);
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
}
