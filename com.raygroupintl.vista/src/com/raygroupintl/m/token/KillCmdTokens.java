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
import com.raygroupintl.m.parsetree.KillCmdNodes;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;

public final class KillCmdTokens {
	public static final class MKillCmd extends MCommand {
		public MKillCmd(Token cmdName, Token cmdDependent) {
			super(cmdName, cmdDependent);
		}		
		
		@Override
		protected String getFullName() {		
			return "KILL";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			if (argumentNode == null) {
				return new KillCmdNodes.AllKillCmd(postConditionNode);
			} else {
				return new KillCmdNodes.KillCmd(postConditionNode, argumentNode);				
			}
		}
	}
	
	public static final class MExclusiveAtomicKillCmd extends MSequence {
		public MExclusiveAtomicKillCmd(int length) {
			super(length);
		}
		
		public MExclusiveAtomicKillCmd(Tokens store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			MDelimitedList list = (MDelimitedList) this.getToken(1);
			Nodes<Node> nodes = NodeUtilities.getNodes(list, list.size());
			return new KillCmdNodes.ExclusiveAtomicKill(nodes);
		}		
	}

	public static final class MAtomicKillCmd extends MTokenCopy {
		public MAtomicKillCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new KillCmdNodes.AtomicKill(subNode);
		}		
	}
	
	public static final class MKilledLocal extends MString {
		private static final long serialVersionUID = 1L;
		
		public MKilledLocal(Token token) {
			super(token);
		}

		@Override
		public Node getNode() {
			return new Local(this.toValue());
		}		
	} 	
}
