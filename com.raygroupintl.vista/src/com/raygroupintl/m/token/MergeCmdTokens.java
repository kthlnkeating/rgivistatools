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
import com.raygroupintl.m.parsetree.MergeCmdNodes;
import com.raygroupintl.parser.Token;

public final class MergeCmdTokens {
	public static final class MMergeCmd extends MCommand {
		public MMergeCmd(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new MergeCmdNodes.MergeCmd(postConditionNode, argumentNode);	
		}
	}
	
	public static final class MAtomicMergeCmd extends MSequence {
		public MAtomicMergeCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			MToken lhs = (MToken) this.get(0);
			Node lhsNode = lhs.getNode();
			MToken rhs = (MToken) this.get(2);
			Node rhsNode = rhs.getNode();
			return new MergeCmdNodes.AtomicMerge(lhsNode, rhsNode);
		}		
	}
	
	public static final class MIndirectAtomicMergeCmd extends MSequence {
		public MIndirectAtomicMergeCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			MToken lhs = (MToken) this.get(0);
			Node lhsNode = lhs.getNode();
			MSequence rhsSeq = (MSequence) this.get(1);
			if (rhsSeq == null) {
				return new MergeCmdNodes.IndirectAtomicMerge(lhsNode);
			} else {
				MToken rhs = (MToken) rhsSeq.get(1);				
				Node rhsNode = rhs.getNode();
				return new MergeCmdNodes.AtomicMerge(lhsNode, rhsNode);
			}
		}		
	}
}
