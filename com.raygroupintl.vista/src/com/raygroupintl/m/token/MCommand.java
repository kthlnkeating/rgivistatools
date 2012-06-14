package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TEmpty;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Token;

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

public abstract class MCommand extends MSequence {
	public MCommand(Token token) {
		super(token);
	}
	
	protected Node getArgumentNode() {
		MSequence nameFollowUp = (MSequence) this.get(1);
		if (nameFollowUp == null) {
			return null;
		}
		if (nameFollowUp.get(2) instanceof TEmpty) {
			return null;
		}			
		MToken argument = (MToken) nameFollowUp.get(2);
		if ((argument == null) || (argument.toValue().length() == 0)) {
			return null;
		} else {				
			return argument.getNode();
		}
	}

	protected Node getPostConditionNode() {
		MSequence nameFollowUp = (MSequence) this.get(1);
		if (nameFollowUp == null) {
			return null;
		}
		MSequence postConditionWithColon = (MSequence) nameFollowUp.get(0);
		if (postConditionWithColon == null) {
			return null;
		} else {
			MToken postConditionToken = (MToken) postConditionWithColon.get(1);
			return postConditionToken.getNode();
		}
	}

	protected abstract String getFullName();

	protected Node getNode(Node postConditionNode, Node argumentNode) {
		return new GenericCommand(postConditionNode, argumentNode);	
	}
	
	@Override
	public void beautify() {
		TString n = (TString) this.get(0);
		StringPiece newName = new StringPiece(getFullName());
		n.setValue(newName);
		super.beautify();
	}
			
	@Override
	public Node getNode() {
		Node postConditionNode = this.getPostConditionNode();
		Node argumentNode = this.getArgumentNode();
		return this.getNode(postConditionNode, argumentNode);
	}	
}

