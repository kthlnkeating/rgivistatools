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

import com.raygroupintl.struct.IterableSingle;

public class TString extends StringPiece implements Token, TokenStore {
	private static final long serialVersionUID = 1L;
	
	public TString() {
		super();
	}

	public TString(StringPiece value) {
		super(value);
	}
	
	public TString(Token token) {
		super(token.toValue());
	}
	
	public TString(String data, int beginIndex, int endIndex) {
		super(data, beginIndex, endIndex);
	}
	
	@Override
	public void addToken(Token token) {
		this.add(token.toValue());
	}
	
	@Override
	public StringPiece toValue() {
		return this;
	}
	
	@Override
	public List<Token> toList() {	
		List<Token> result = new ArrayList<Token>();
		result.add(this);	
		return result;
	}

	@Override
	public void setLength(int length) {		
	}

	@Override
	public void resetIndex(int index) {		
	}
	
	@Override
	public boolean isAllNull() {
		return false;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean hasToken() {
		return true;
	}
	
	@Override
	public Token get(int index) {
		if (index == 0) {
			return this;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public void set(int index, Token token) {
		throw new UnsupportedOperationException();
	}	
	
	public void setValue(StringPiece value) {
		super.set(value);
	}

	@Override
	public Iterator<Token> iterator() {
		return new IterableSingle<Token>(this).iterator();
	}

	@Override
	public void beautify() {		
	}
}
