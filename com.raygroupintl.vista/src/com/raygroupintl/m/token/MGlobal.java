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

import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;

public class MGlobal extends MSequence {
	public MGlobal(int length) {
		super(length);
	}

	public MGlobal(TokenStore store) {
		super(store);
	}

	@Override
	public Node getNode() {
		TokenStore actual = (TokenStore) this.get(1);
		if (actual.get(0) != null) {
			return NodeUtilities.getNodes(actual, actual.size());
		} else {
			StringPiece name = actual.get(1).toValue();
			Token subsripts = actual.get(2);
			if (subsripts == null) {
				return new Global(name);
			} else {
				NodeList<Node> nodes = NodeUtilities.getSubscriptNodes(subsripts);
				return new Global(name, nodes);
			}
		}
	}
}
