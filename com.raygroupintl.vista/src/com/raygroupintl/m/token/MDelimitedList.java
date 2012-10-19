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
import com.raygroupintl.parser.CompositeToken;
import com.raygroupintl.parser.DelimitedListOfTokens;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public class MDelimitedList extends DelimitedListOfTokens implements MToken, CompositeToken {
	public MDelimitedList(Token leadingToken, Tokens tailTokens) {
		super(leadingToken, tailTokens);
	}

	@Override
	public Node getNode() {
		return NodeUtilities.getNodes(this, this.size());
	}

	@Override
	public Node getSubNode(int index) {
		MToken subToken = this.getSubNodeToken(index);
		if (subToken != null) {
			return subToken.getNode();
		}
		return null;
	}

	@Override
	public Node getSubNode(int index0, int index1) {
		MToken subToken = (MToken) this.getToken(index0);
		if (subToken != null) {
			subToken = (MToken) this.getToken(index1);
			if (subToken != null) return subToken.getNode();
		}
		return null;
	}	

	@Override
	public int getNumSubNodes() {
		return this.size();
	}

	@Override
	public MToken getSubNodeToken(int index) {
		MToken subToken = (MToken) this.getToken(index);
		if (subToken != null) {
			if (index == 0) {
				return subToken;
			} else {
				return subToken.getSubNodeToken(1);
			}
		}
		return null;
	}

	@Override
	public void beautify() {
		for (Token token : this.all()) {
			token.beautify();
		}
	}
}
