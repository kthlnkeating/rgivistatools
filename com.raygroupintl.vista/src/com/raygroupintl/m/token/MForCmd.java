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

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.ForLoop;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

public class MForCmd extends MCommandBase {
	public MForCmd(Token token) {
		super(token);
	}

	@Override
	protected String getFullName() {		
		return "FOR";
	}			

	@Override
	public Node getNode() {
		MToken argument = this.getArgument();
		if (argument == null) {
			return new ForLoop();
		} else {
			List<Token> tokens = argument.toList();
			MToken lhs = (MToken) tokens.get(0);
			TList rhss = (TList) tokens.get(2);
			List<Node[]> nodes = new ArrayList<Node[]>(rhss.size());
			for (Token t : rhss) {
				List<Token> ts = t.toList();
				int size = ts.size();
				if ((size > 2) && (ts.get(2) == null)) size = 2;
				if ((size > 1) && (ts.get(1) == null)) size = 1;
				Node[] a = new Node[size];
				a[0] = ((MToken) ts.get(0)).getNode();				
				for (int i=1; i<size; ++i) {
					MToken ti = (MToken) ts.get(i).toList().get(1);
					a[i] = ti.getNode();
				}
				nodes.add(a);
			}	
			return new ForLoop(lhs.getNode(), nodes);
		}
	}	
}