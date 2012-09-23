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

public class TSequence implements Token, TokenStore {
	private List<Token> tokens;
	int index = 0;
	int length = 0;
	
	public TSequence(List<Token> tokens) {
		this.tokens = tokens;
		this.index = tokens.size();
		this.length = tokens.size();
	}

	public TSequence(TSequence token) {
		this.tokens = token.tokens;
		this.index = token.index;
		this.length = token.length;
	}
	
	public TSequence(Token token) {
		this.tokens = token.toList();
		this.index = tokens.size();
		this.length = tokens.size();
	}
	
	public TSequence(int length) {
		this.length = length;
	}

	@Override
	public StringPiece toValue() {	
		StringPiece result = new StringPiece();
		for (int i=0; i<this.index; ++i) {
			Token t = this.tokens.get(i);
			if (t != null) {
				result.add(t.toValue());
			}
		}
		return result;
	}

	@Override
	public void beautify() {		
		for (int i=0; i<this.index; ++i) {
			Token t = this.tokens.get(i);
			if (t != null) {
				t.beautify();
			}
		}
	}
	
	public int size() {
		return this.index;
	}
	
	@Override
	public Token get(int i) {
		if (this.index > i) {
			return this.tokens.get(i);
		} else {
			return null;
		}
	}
	
	@Override
	public Iterator<Token> iterator() {
		if (this.tokens == null) {
			return Collections.emptyListIterator();
		} else {
			return this.tokens.iterator();
		}
	}
	
	@Override
	public List<Token> toList() {
		return this.tokens;
	}

	@Override
	public void addToken(Token token) {
		++index;
		if (token == null) {
			if (this.tokens != null) {
				this.tokens.add(token);
			}
		} else {
			if (this.tokens == null) {
				this.tokens = new ArrayList<Token>(this.length);				
				for (int i=0; i<index-1; ++i) this.tokens.add(null);
			}
			this.tokens.add(token);
		}
	}
	
	public void setLength(int length) {
		this.length = length;
		for (int i = this.index; index<tokens.size(); ++index) {
			this.tokens.set(i, null);
		}				
	}
	
	@Override
	public boolean isAllNull() {
		return this.tokens == null;
	}

	public void resetIndex(int index) {
		this.index = index;
	}
	
	@Override
	public boolean hasToken() {
		return this.index > 0;
	}

	public void set(int index, Token token) {
		this.tokens.set(index, token);
	}
}
