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

package com.raygroupintl.m.parsetree;

public class AtomicSet extends BasicNode {
	private NodeList<Node> lhss;
	private Node rhs;
	
	public AtomicSet(NodeList<Node> lhss, Node rhs) {
		this.lhss = lhss;
		this.rhs = rhs;
	}
	
	public void acceptSubNodes(Visitor visitor) {
		this.rhs.accept(visitor);
		this.lhss.accept(visitor);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitAtomicSet(this);
	}	
}
