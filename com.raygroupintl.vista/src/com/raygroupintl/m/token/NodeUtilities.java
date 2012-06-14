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
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.parser.TList;
import com.raygroupintl.parser.Token;

class NodeUtilities {
	static Nodes<Node> getNodes(Iterable<Token> iterable, int size) {
		NodeList<Node> result = new NodeList<Node>(size);
		for (Token t : iterable) {
			if ((t != null) && (t instanceof MToken)) {
				MToken mt = (MToken) t;
				Node node = mt.getNode();
				if (node != null) {
					result.add(node);
				}
			}
		}
		return result;		
	}
	
	static NodeList<Node> getSubscriptNodes(Token subscriptToken) {
		TList subscripts = (TList) subscriptToken.toList().get(1);
		int size = subscripts.size();
		NodeList<Node> nodes = new NodeList<>(size);
		for (Token t : subscripts) {
			Node node = ((MToken) t).getNode();
			nodes.add(node);
		}
		return nodes;
	}	
}
