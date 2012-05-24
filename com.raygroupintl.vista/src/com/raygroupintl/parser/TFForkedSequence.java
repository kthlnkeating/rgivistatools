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
	private TFSequence.TFSequenceCopy copy;
	
	public TFForkedSequence(String name) {
		super(name);
	}

	public TFForkedSequence(String name, TokenFactory leader) {
		super(name);
		this.leader = leader;
	}
	
	public void addFollower(TokenFactory follower) {
		if ((follower == this.leader) || (follower instanceof TFSequence.TFSequenceCopy)) {
			this.singleValid = true;
			if (follower instanceof TFSequence.TFSequenceCopy) {
				this.copy = (TFSequence.TFSequenceCopy) follower;
			}
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
	
	@Override
	public Token tokenize(Text text, AdapterSupply adapterSupply) throws SyntaxErrorException {
		Token leading = this.leader.tokenize(text, adapterSupply);
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
			if (this.copy != null) {
				return this.copy.convert((TSequence) leading);
			} else {
				return leading;
			}
		}
		throw new SyntaxErrorException();
	}
}
