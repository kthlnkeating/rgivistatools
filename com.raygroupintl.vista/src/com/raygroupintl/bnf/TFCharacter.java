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

package com.raygroupintl.bnf;

import com.raygroupintl.charlib.Predicate;

public class TFCharacter extends TokenFactory {
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
	public Token tokenize(Text text) {
		return text.extractToken(this.predicate, this.adapter);
	}
}
