//---------------------------------------------------------------------------
//Copyright 2012 Ray Group International
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.Empty;

public abstract class MCommand extends MCommandBase {
	public MCommand(MToken token0, MToken token1) {
		super(token0, token1);
	}
	
	protected Node getArgumentNode() {
		MSequence nameFollowUp = (MSequence) this.getToken(1);
		if (nameFollowUp == null) {
			return null;
		}
		if (nameFollowUp.getToken(2) instanceof Empty) {
			return null;
		}			
		MToken argument = nameFollowUp.getToken(2);
		if ((argument == null) || (argument.toValue().length() == 0)) {
			return null;
		} else {				
			return argument.getNode();
		}
	}

	protected Node getPostConditionNode() {
		MToken t = this.getToken(1, 0, 1);
		return (t == null) ? null : t.getNode();
	}

	protected Node getNode(Node postConditionNode, Node argumentNode) {
		return new GenericCommand(postConditionNode, argumentNode);	
	}
	
	@Override
	public Node getNode() {
		Node postConditionNode = this.getPostConditionNode();
		Node argumentNode = this.getArgumentNode();
		return this.getNode(postConditionNode, argumentNode);
	}	
}

