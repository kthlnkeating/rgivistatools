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

import com.raygroupintl.parser.annotation.AdapterSupply;
import com.raygroupintl.parser.annotation.ParseErrorException;

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
	
	public void addFollower(TokenFactory follower) {
		if (follower.getSequenceCount() == 1) {
			this.singleValid = true;
			this.leader = follower;
			return;
		}
		if (follower instanceof TFSequence) {			
			if (this.followers == null) {
				this.followers = new ArrayList<TFSequence>();
			}
			this.followers.add((TFSequence) follower);
		} else {
			throw new ParseErrorException("Forked sequence only supports objects of kind " + TFSequence.class.getName());
		}
	}
	
	public void set(List<TFSequence> followers) {
		this.followers = followers;
	}
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		Token leading = this.leader.tokenizeRaw(text, adapterSupply);
		if (leading == null) {
			return null;
		}
		if (text.onChar()) {
			int textIndex = text.getIndex();
			for (TFSequence follower : this.followers) {
				TokenStore foundTokens = new ArrayAsTokenStore(follower.getSequenceCount());
				foundTokens.addToken(leading);
				Token result = follower.tokenize(text, adapterSupply, 1, foundTokens, true);
				if (result != null) {
					return result;
				}
				text.resetIndex(textIndex);				
			}
		} else {
			for (TFSequence follower : this.followers) {
				TokenStore foundTokens = new ArrayAsTokenStore(follower.getSequenceCount());
				foundTokens.addToken(leading);
				if (follower.validateEnd(0, foundTokens, true)) {
					return follower.getToken(foundTokens, adapterSupply);
				}
			}
		}
		if (this.singleValid) {
			return this.leader.convert(leading);
		}
		throw new SyntaxErrorException();
	}
}
