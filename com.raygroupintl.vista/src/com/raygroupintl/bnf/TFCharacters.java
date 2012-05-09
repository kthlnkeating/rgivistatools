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

public class TFCharacters implements TokenFactory {
	private Predicate predicate;
	private CharactersAdapter adapter;
	
	public TFCharacters(Predicate predicate) {
		this.predicate = predicate;
	}
		
	protected Token getToken(String value) {
		if (this.adapter == null) {
			return new TCharacters(value);
		} else {
			return this.adapter.convert(value);
		}
	}
	
	public void setAdapter(CharactersAdapter adapter) {
		this.adapter = adapter;
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) {
		int endIndex = line.length();
		int index = fromIndex;
		while (index < endIndex) {
			char ch = line.charAt(index);
			if (! this.predicate.check(ch)) {
				if (fromIndex == index) {
					return null;
				} else {
					return this.getToken(line.substring(fromIndex, index));
				}
			}
			++index;
		}
		if (fromIndex < endIndex) {
			return this.getToken(line.substring(fromIndex));			
		} else {
			return null;
		}
	}
}
