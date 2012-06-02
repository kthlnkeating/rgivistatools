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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TList implements Token, TokenStore, Iterable<Token> {
	private List<Token> tokens;
		
	public TList() {
	}
	
	public TList(List<Token> tokens) {
		this.tokens = tokens;
	}

	@Override
	public void addToken(Token token) {
		if (this.tokens == null) {
			this.tokens = new ArrayList<Token>();
		}
		this.tokens.add(token);
	}

	@Override
	public List<Token> toList() {
		if (this.tokens == null) {
			return Collections.emptyList();
		} else {
			return this.tokens;
		}
	}

	@Override
	public boolean isAllNull() {
		return (this.tokens == null) || (this.tokens.size() == 0);
	}

	@Override
	public int size() {
		return this.tokens == null ? 0 : this.tokens.size();
	}

	@Override
	public boolean hasToken() {
		return this.tokens != null;
	}
	
	public void set(int index, Token token) {
		this.tokens.set(index, token);
	}

	@Override
	public StringPiece toValue() {	
		StringPiece result = new StringPiece();
		if (this.tokens != null) for (Token t : this.tokens) if (t != null) {
			result.add(t.toValue());
		}		
		return result;
	}

	@Override
	public void beautify() {
		if (this.tokens != null) for (Token token : this.tokens) {
			token.beautify();
		}
	}
	
	public Token get(int index) {
		return this.tokens == null ? null : this.tokens.get(index);
	}
	
	@Override
	public Iterator<Token> iterator() {
		return this.toList().iterator();
	}
}
