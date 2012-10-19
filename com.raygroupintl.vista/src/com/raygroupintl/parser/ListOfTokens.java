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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListOfTokens extends CollectionOfTokens {
	protected List<Token> tokens;

	public ListOfTokens() {
	}
	
	public ListOfTokens(ListOfTokens rhs) {
		this.tokens = rhs.tokens;
	}

	@Override
	public void setToken(int index, Token token) {
		this.tokens.set(index, token);
	}

	@Override
	public void addToken(Token token) {
		if (this.tokens == null) {
			this.tokens = new ArrayList<Token>();
		}
		this.tokens.add(token);
	}

	@Override
	public Token getToken(int index) {
		return this.tokens == null ? null : this.tokens.get(index);
	}
	
	@Override
	public int size() {
		return this.tokens == null ? 0 : this.tokens.size();
	}

	@Override
	public boolean hasToken() {
		return this.tokens != null;
	}
	
	@Override
	public StringPiece toValue() {	
		return TokenUtilities.toValue(this);
	}

	@Override
	public Iterator<Token> iterator() {
		return this.tokens.iterator();
	}
}
