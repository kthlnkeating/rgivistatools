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
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokensVisitor;

public abstract class MTokenCopy implements MToken {
	private MToken actual;
	
	public MTokenCopy(Token token) {
		this.actual = (MToken) token;
	}

	protected abstract Node getNode(Node subNode);

	@Override
	public StringPiece toValue() {
		return this.actual.toValue();
	}
	
	@Override
	public void beautify() {		
	}	
	
	
	@Override
	public Node getNode() {
		Node node = this.actual.getNode();
		return this.getNode(node);		
	}

	@Override
	public Node getSubNode(int index) {
		return this.actual.getSubNode(index);
	}

	@Override
	public Node getSubNode(int index0, int index1) {
		return this.actual.getSubNode(index0, index1);
	}	
	
	@Override
	public int getNumSubNodes() {
		return this.actual.getNumSubNodes();
	}

	@Override
	public MToken getSubNodeToken(int index) {
		return this.actual.getSubNodeToken(index);
	}
	
	public void accept(TokensVisitor visitor) {
		this.actual.accept(visitor);
	}	
}
