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

import java.util.List;

public class ForLoop extends BasicNode {
	private Node lhs;
	private List<Node[]> rhss;
	private Nodes<Node> loopNodes;
		
	public ForLoop() {		
	}

	public ForLoop(Node lhs, List<Node[]> rhss) {
		this.lhs = lhs;
		this.rhss = rhss;
	}

	public void setLoopNodes(Nodes<Node> loopNodes) {
		this.loopNodes = loopNodes;
	}
	
	public void acceptSubNodes(Visitor visitor) {
		if (this.lhs != null) {
			this.lhs.acceptPreAssignment(visitor);
			for (Node[] rhs : this.rhss) {
				for (int i=0; i<rhs.length; ++i) {
					rhs[i].accept(visitor);
				}
			}
			this.lhs.acceptPostAssignment(visitor);
		}
		if (this.loopNodes != null) {
			for (Node node : this.loopNodes.getNodes()) {
				node.accept(visitor);
			}
		}
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitForLoop(this);
	}
}
