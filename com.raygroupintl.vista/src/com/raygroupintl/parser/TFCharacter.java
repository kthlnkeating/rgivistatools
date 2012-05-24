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
import com.raygroupintl.parser.annotation.AdapterSupply;

public class TFCharacter extends TFBasic {
	private static final CharacterAdapter DEFAULT_ADAPTER = new CharacterAdapter() {
		@Override
		public Token convert(char value) {
			return new TChar(value);
		}
	}; 
	
	private Predicate predicate;
	private CharacterAdapter adapter;
	
	public TFCharacter(String name, Predicate predicate) {
		this(name, predicate, DEFAULT_ADAPTER);
	}

	public TFCharacter(String name, Predicate predicate, CharacterAdapter adapter) {
		super(name);
		this.predicate = predicate;
		this.adapter = adapter == null ? DEFAULT_ADAPTER : adapter;
	}
	
	@Override
	public void copyWoutAdapterFrom(TFBasic rhs) {
		if (rhs instanceof TFCharacter) {
			this.predicate = ((TFCharacter) rhs).predicate;
		} else {
			throw new IllegalArgumentException("Illegal attemp to copy from " + rhs.getClass().getName() + " to " + TFCharacter.class.getName());
		}
	}
	
	@Override
	public TFBasic getCopy(String name) {
		return new TFCharacter(name, this.predicate, this.adapter);
	}
	
	@Override
	public boolean isInitialized() {
		return true;
	}
	
	public void setAdapter(CharacterAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) {
		return text.extractToken(this.predicate, this.adapter);
	}

	@Override
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, char.class, TChar.class);
		this.adapter = new CharacterAdapter() {			
			@Override
			public Token convert(char ch) {
				try{
					return (Token) constructor.newInstance(ch);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
	
	@Override
	public void setAdapter(Object adapter) {
		if (adapter instanceof CharacterAdapter) {
			this.adapter = (CharacterAdapter) adapter;					
		} else {
			throw new IllegalArgumentException("Wrong adapter type " + adapter.getClass().getName());
		}
	}	
}
