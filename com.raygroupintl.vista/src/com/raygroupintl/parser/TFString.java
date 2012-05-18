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

import com.raygroupintl.charlib.Predicate;

public class TFString extends TFBasic {
	private static final StringAdapter DEFAULT_ADAPTER = new StringAdapter() {
		@Override
		public Token convert(String value) {
			return new TString(value);
		}
	}; 
	
	private Predicate predicate;
	private StringAdapter adapter;
	
	public TFString(String name, Predicate predicate) {
		this(name, predicate, DEFAULT_ADAPTER);
	}
		
	public TFString(String name, Predicate predicate, StringAdapter adapter) {
		super(name);
		this.predicate = predicate;
		this.adapter = adapter == null ? DEFAULT_ADAPTER : adapter;
	}
		
	public void setAdapter(StringAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Token tokenize(Text text) {
		return text.extractToken(this.predicate, this.adapter);
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = this.getConstructor(cls, String.class, TString.class);
		this.adapter = new StringAdapter() {			
			@Override
			public Token convert(String value) {
				try{
					return (Token) constructor.newInstance(value);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
	
	@Override
	public void setAdapter(Object adapter) {
		if (adapter instanceof StringAdapter) {
			this.adapter = (StringAdapter) adapter;					
		} else {
			throw new IllegalArgumentException("Wrong adapter type " + adapter.getClass().getName());
		}
	}	
}
