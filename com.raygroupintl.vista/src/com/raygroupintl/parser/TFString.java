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

import com.raygroupintl.charlib.Predicate;

public class TFString extends TokenFactory {
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
		
	@Override
	public Token tokenize(Text text) {
		return text.extractToken(this.predicate, this.adapter);
	}
}
