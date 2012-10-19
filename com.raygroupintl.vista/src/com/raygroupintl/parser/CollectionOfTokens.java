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

import java.util.List;

public abstract class CollectionOfTokens implements Tokens {
	private static TokensVisitor TOKENS_VISITOR = new TokensVisitor();
		
	protected List<Token> tokens;

	@Override
	public void addToken(int index, Token token) {
		this.tokens.add(index, token);
	}

	@Override
	public void setToken(int index, Token token) {
		this.tokens.set(index, token);
	}

	@Override
	public Token get(int index0, int index1) {
		Tokens ts = this.getTokens(index0);
		if (ts == null) {
			return null;
		} else {
			return ts.get(index1);
		}
	}
	
	@Override
	public Token get(int index0, int index1, int index2) {
		Tokens ts = this.getTokens(index0);
		if (ts == null) {
			return null;
		} else {
			return ts.get(index1, index2);
		}
	}

	@Override
	public Tokens getTokens(int index) {
		Token t = this.get(index);
		if (t == null) {
			return null;
		} else {
			return TOKENS_VISITOR.toTokenStore(t);
		}
	}

	public void accept(TokensVisitor visitor) {
		visitor.visitMultiple(this);
	}
}
