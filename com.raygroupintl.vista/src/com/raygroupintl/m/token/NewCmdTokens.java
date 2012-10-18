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

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.NewCmdNodes;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public final class NewCmdTokens {
	public static final class MNewCmd extends MCommand {
		public MNewCmd(Token cmdName, Token cmdDependent) {
			super(cmdName, cmdDependent);
		}		
		
		@Override
		protected String getFullName() {		
			return "NEW";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			if (argumentNode == null) {
				return new NewCmdNodes.AllNewCmd(postConditionNode);
			} else {
				return new NewCmdNodes.NewCmd(postConditionNode, argumentNode);				
			}
		}
	}
	
	public static final class MExclusiveAtomicNewCmd extends MSequence {
		public MExclusiveAtomicNewCmd(int length) {
			super(length);
		}
		
		public MExclusiveAtomicNewCmd(Tokens store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			MDelimitedList list = (MDelimitedList) this.get(1);
			Nodes<Node> nodes = NodeUtilities.getNodes(list, list.size());
			return new NewCmdNodes.ExclusiveAtomicNew(nodes);
		}		
	}

	public static final class MAtomicNewCmd extends MTokenCopy {
		public MAtomicNewCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new NewCmdNodes.AtomicNew(subNode);
		}		
	}
	
	public static final class MNewedLocal extends MString {
		private static final long serialVersionUID = 1L;
		
		public MNewedLocal(Token token) {
			super(token);
		}

		@Override
		public Node getNode() {
			return new Local(this.toValue());
		}		
	} 	
}
