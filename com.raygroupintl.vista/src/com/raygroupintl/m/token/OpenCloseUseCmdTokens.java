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
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.parser.Token;

public class OpenCloseUseCmdTokens {
	public static final class MOpenCmd extends MCommand {
		public MOpenCmd(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.OpenCmd(postConditionNode, argumentNode);				
		}
	}

	public static final class MAtomicOpenCmd extends MTokenCopy {
		public MAtomicOpenCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new OpenCloseUseCmdNodes.AtomicOpenCmd(subNode);
		}		
	}
	
	public static final class MCloseCmd extends MCommand {
		public MCloseCmd(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.CloseCmd(postConditionNode, argumentNode);				
		}
	}

	public static final class MAtomicCloseCmd extends MTokenCopy {
		public MAtomicCloseCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new OpenCloseUseCmdNodes.AtomicCloseCmd(subNode);
		}		
	}

	public static final class MUseCmd extends MCommand {
		public MUseCmd(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.UseCmd(postConditionNode, argumentNode);				
		}
	}

	public static final class MAtomicUseCmd extends MTokenCopy {
		public MAtomicUseCmd(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new OpenCloseUseCmdNodes.AtomicUseCmd(subNode);
		}		
	}

	public static class MDeviceParameters extends MSequence {
		public MDeviceParameters(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			Nodes<Node> nodes = NodeUtilities.getNodes(this, this.size());
			OpenCloseUseCmdNodes.DeviceParameters result = new OpenCloseUseCmdNodes.DeviceParameters(nodes);
			return result;
		}
	}	
}
