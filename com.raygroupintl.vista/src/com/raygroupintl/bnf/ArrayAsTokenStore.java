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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.raygroupintl.struct.IterableArray;

public class ArrayAsTokenStore implements TokenStore {
	private List<Token> tokens;
	int index = 0;
	
	public ArrayAsTokenStore(int length) {
		this.tokens = new ArrayList<Token>(length);
	}
	
	@Override
	public void addToken(Token token) {
		this.tokens.add(token);
		++index;
	}
	
	@Override
	public List<Token> toList() {
		return this.tokens;
	}
	
	@Override
	public Token toToken() {
		return new TArray(this.tokens);		
	}
		
	@Override
	public Iterator<Token> iterator() {
		return this.tokens.iterator();
	}

	@Override
	public Token get(int index) {
		if (index < this.tokens.size()) {
			return this.tokens.get(index);
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return this.index;
	}

	@Override
	public boolean hasToken() {
		return this.index > 0;
	}
}
