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

import java.util.List;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.Token;

public class MTSequence extends TSequence implements MToken {
	public MTSequence(List<Token> tokens) {
		super(tokens);
	}

	public MTSequence(Token token) {
		super(token.toList());
	}

	public MTSequence(int length) {
		super(length);
	}
	
	@Override
	public Node getNode() {
		return NodeUtilities.getNodes(this);
	}
}
