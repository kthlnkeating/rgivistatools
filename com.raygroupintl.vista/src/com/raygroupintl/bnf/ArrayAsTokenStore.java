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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.raygroupintl.struct.IterableArray;

public class ArrayAsTokenStore implements TokenStore {
	private Token[] tokens;
	int index = 0;
	
	public ArrayAsTokenStore(int length) {
		this.tokens = new Token[length];
	}
	
	@Override
	public void addToken(Token token) {
		this.tokens[this.index] = token;
		++index;
	}
	
	@Override
	public Token[] toArray() {
		return this.tokens;
	}
	
	@Override
	public List<Token> toList() {
		return Arrays.asList(this.tokens);
	}
	
	
	@Override
	public Iterator<Token> iterator() {
		return new IterableArray<Token>(this.tokens).iterator();
	}

	@Override
	public Token get(int index) {
		return this.tokens[index];
	}
}
