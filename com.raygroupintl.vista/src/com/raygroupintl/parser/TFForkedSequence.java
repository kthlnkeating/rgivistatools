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

import com.raygroupintl.parsergen.ObjectSupply;

public class TFForkedSequence extends TokenFactory {
	private TokenFactory leader;
	private List<TFSequence> followers;
	private boolean singleValid;
	
	public TokenFactory getLeader() {
		return this.leader;
	}
	
	public List<TFSequence> getFollowers() {
		return this.followers;
	}
	
	public boolean isSingleValid() {
		return this.singleValid;
	}
	
	
	public TFForkedSequence(String name) {
		super(name);
	}

	public TFForkedSequence(String name, TokenFactory leader) {
		super(name);
		this.leader = leader;
	}
	
	public TFForkedSequence(String name, TokenFactory leader, boolean singleValid) {
		super(name);
		this.leader = leader;
		this.singleValid = singleValid;
	}
	
	public void setFollowers(List<TFSequence> followers) {
		this.followers = followers;
	}
	
	public void set(List<TFSequence> followers) {
		this.followers = followers;
	}
	
	private int getMaxSequenceCount() {
		int result = 0;
		for (TFSequence follower : this.followers) {
			int count = follower.getSequenceCount();
			if (count > result) return result;
		}
		return result;
	}
	
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		Token leading = this.leader.tokenizeOnly(text, objectSupply);
		if (leading == null) {
			return null;
		}
		CompositeToken foundTokens = objectSupply.newSequence(this.getMaxSequenceCount());
		foundTokens.addToken(leading);
		if (text.onChar()) {
			int textIndex = text.getIndex();
			for (TFSequence follower : this.followers) {
				foundTokens.resetIndex(1);
				CompositeToken result = follower.tokenizeCommon(text, objectSupply, 1, foundTokens, true);
				if (result != null) {
					TokenFactory f0th = follower.getFactory(0);
					Token replaced = f0th.convert(leading);
					foundTokens.set(0, replaced);
					foundTokens.setLength(follower.getSequenceCount());
					return follower.convertSequence(result);
				}
				text.resetIndex(textIndex);				
			}
		} else {
			for (TFSequence follower : this.followers) {
				if (follower.validateEnd(0, foundTokens, true)) {
					return follower.convertSequence(foundTokens);
				}
			}
		}
		if (this.singleValid) {
			return this.leader.convert(leading);
		}
		throw new SyntaxErrorException();
	}
}
