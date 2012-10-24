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
import java.util.List;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFForkedSequence<T extends Token> extends TokenFactory<T> {
	private TokenFactory<T> leader;
	private List<TFSequence<T>> followers;
	private boolean singleValid;
	
	public TokenFactory<T> getLeader() {
		return this.leader;
	}
	
	public List<TFSequence<T>> getFollowers() {
		return this.followers;
	}
	
	public boolean isSingleValid() {
		return this.singleValid;
	}
	
	
	public TFForkedSequence(String name) {
		super(name);
	}

	public TFForkedSequence(String name, TokenFactory<T> leader) {
		super(name);
		this.leader = leader;
	}
	
	public TFForkedSequence(String name, TokenFactory<T> leader, boolean singleValid) {
		super(name);
		this.leader = leader;
		this.singleValid = singleValid;
	}
	
	public void addFollower(TFSequence<T> follower) {
		if (this.followers == null) {
			this.followers = new ArrayList<TFSequence<T>>();
		}
		this.followers.add(follower);
	}
	
	public void setLeader(TokenFactory<T> leader) {
		this.leader = leader;
	}
	
	public void setSingleValid(boolean b) {
		this.singleValid = b;
	}
	
	private int getMaxSequenceCount() {
		int result = 0;
		for (TFSequence<T> follower : this.followers) {
			int count = follower.getSequenceCount();
			if (count > result) return result;
		}
		return result;
	}
	
	@Override
	public T tokenize(Text text, ObjectSupply<T> objectSupply) throws SyntaxErrorException {
		T leading = (this.leader instanceof TFWithAdaptor) ? ((TFWithAdaptor<T>) this.leader).tokenizeOnly(text, objectSupply) : this.leader.tokenize(text, objectSupply);
		if (leading == null) {
			return null;
		}
		SequenceOfTokens<T> foundTokens = new SequenceOfTokens<T>(this.getMaxSequenceCount());
		foundTokens.addToken(leading);
		if (text.onChar()) {
			int textIndex = text.getIndex();
			for (TFSequence<T> follower : this.followers) {
				foundTokens.resetIndex(1);
				SequenceOfTokens<T> result = follower.tokenizeCommon(text, objectSupply, 1, foundTokens, true);
				if (result != null) {
					TokenFactory<T> f0th = follower.getFactory(0);
					T replaced = f0th.convertToken(leading);
					foundTokens.setToken(0, replaced);
					foundTokens.setLength(follower.getSequenceCount());
					return follower.convertSequence(result, objectSupply);
				}
				text.resetIndex(textIndex);				
			}
		} else {
			for (TFSequence<T> follower : this.followers) {
				if (follower.validateEnd(0, foundTokens, true)) {
					return follower.convertSequence(foundTokens, objectSupply);
				}
			}
		}
		if (this.singleValid) {
			return this.leader.convertToken(leading);
		}
		throw new SyntaxErrorException();
	}
}
