//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.pwc.us.rgi.m.token;

import com.pwc.us.rgi.m.parsetree.Node;
import com.pwc.us.rgi.m.parsetree.PostConditional;
import com.pwc.us.rgi.parser.SequenceOfTokens;

public class MLineOffset extends MSequence {
	public MLineOffset(int length) {
		super(length);
	}
	
	public MLineOffset(SequenceOfTokens<MToken> tokens) {
		super(tokens);
	}
	
	@Override
	public Node getNode() {
		Node addlNode = this.getToken(1).getNode();
		return new PostConditional(addlNode);
	}		

}