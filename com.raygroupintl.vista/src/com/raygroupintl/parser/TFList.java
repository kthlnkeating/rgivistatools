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
	
	public void setElement(TokenFactory elementFactory) {
		this.elementFactory = elementFactory;
	}
		
	public TList tokenizeCommon(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		if (elementFactory == null) throw new IllegalStateException("TFList.setElementFactory needs to be called before TFList.tokenize");
		
		if (text.onChar()) {
			TList list = adapterSupply.newList();
			while (text.onChar()) {
				Token token = null;
				try {
					token = this.elementFactory.tokenize(text, adapterSupply);
				} catch (SyntaxErrorException e) {
					throw e;
				}
				if (token == null) {
					if (list.hasToken()) {
						return list;
					} else {
						return null;
					}
				}
				list.addToken(token);	
			}
			return list;
		}
		return null;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		TList rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			if (this.adapter == null) {
				return rawResult;
			} else {
				return this.adapter.convert(rawResult);
			}
		}
	}

	@Override
	public TList tokenizeRaw(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		TList rawResult = this.tokenizeCommon(text, adapterSupply);
		if (rawResult == null) {
			return null;
		} else {
			return rawResult;	
		}
	}

	@Override
	protected Token convert(Token token) {
		if (this.adapter != null) {
			return this.adapter.convert(token); 
		} else {
			return token;
		}
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, Token.class, TList.class);
		this.adapter = new ListAdapter() {			
			@Override
			public Token convert(Token token) {
				try{
					return (Token) constructor.newInstance(token);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
}
