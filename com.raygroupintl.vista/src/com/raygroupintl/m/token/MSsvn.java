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
import com.raygroupintl.m.parsetree.StructuredSystemVariable;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;

public class MSsvn extends MSequence {
	private static final MNameWithMnemonic.Map SSVS = new MNameWithMnemonic.Map();
	static {
		SSVS.update("D", "DEVICE"); 	
		SSVS.update("DI", "DISPLAY"); 	
		SSVS.update("E", "EVENT"); 	
		SSVS.update("G", "GLOBAL"); 	
		SSVS.update("J", "JOB"); 	
		SSVS.update("L", "LOCK"); 	
		SSVS.update("R", "ROUTINE"); 	
		SSVS.update("S", "SYSTEM"); 	
		SSVS.update("W", "WINDOW"); 	
	}
	
	public MSsvn(int length) {
		super(length);
	}
	
	public MSsvn(TokenStore store) {
		super(store);
	}
	
	@Override
	public Node getNode() {
		StringPiece name = this.get(1).toValue();
		Token subsripts = this.get(2);
		NodeList<Node> nodes = NodeUtilities.getSubscriptNodes(subsripts);
		return new StructuredSystemVariable(name, nodes);
	}
}
