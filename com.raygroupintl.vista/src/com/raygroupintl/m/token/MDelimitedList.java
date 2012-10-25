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
import com.raygroupintl.parser.DelimitedListOfTokens;

public class MDelimitedList extends DelimitedListOfTokens<MToken> implements MToken {
	public MDelimitedList(DelimitedListOfTokens<MToken> tokens) {
		super(tokens);
	}

	@Override
	public Node getNode() {
		return NodeUtilities.getNodes(this, this.size());
	}

	@Override
	public void beautify() {
		for (MToken token : this.toIterable()) {
			token.beautify();
		}
	}
}
