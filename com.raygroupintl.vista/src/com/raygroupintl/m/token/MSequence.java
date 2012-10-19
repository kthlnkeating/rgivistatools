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

package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.Token;

public class MSequence extends SequenceOfTokens implements MToken {
	public MSequence(int length) {
		super(length);
	}
	
	public MSequence(SequenceOfTokens tokens) {
		super(tokens);
	}
	
	public MSequence(Token token0, Token token1) {
		super(token0, token1);
	}
		
	@Override
	public Node getNode() {
		return NodeUtilities.getNodes(this, this.size());
	}

	@Override
	public Node getSubNode(int index) {
		MToken subToken = (MToken) this.getToken(index);
		return subToken == null ? null : subToken.getNode();
	}

	@Override
	public Node getSubNode(int index0, int index1) {
		MToken subToken = (MToken) this.getToken(index0);
		if (subToken != null) {
			return subToken.getSubNode(index1);
		}
		return null;
	}	

	@Override
	public int getNumSubNodes() {
		return this.size();
	}

	@Override
	public MToken getSubNodeToken(int index) {
		return (MToken) this.getToken(index);
	}

	@Override
	public void beautify() {		
		for (Token token : this) {
			if (token != null) {
				token.beautify();
			}
		}
	}
}
