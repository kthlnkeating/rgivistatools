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

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.NodeList;
import com.raygroupintl.m.parsetree.StructuredSystemVariable;
import com.raygroupintl.m.struct.KeywordRefactorFlags;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.m.struct.MRefactorSettings;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Tokens;

public class MSsvn extends MSequence {
	private static final Map<String, MNameWithMnemonic> SSVS = new HashMap<String, MNameWithMnemonic>();
	static {
		MNameWithMnemonic.update(SSVS, "D", "DEVICE"); 	
		MNameWithMnemonic.update(SSVS, "DI", "DISPLAY"); 	
		MNameWithMnemonic.update(SSVS, "E", "EVENT"); 	
		MNameWithMnemonic.update(SSVS, "G", "GLOBAL"); 	
		MNameWithMnemonic.update(SSVS, "J", "JOB"); 	
		MNameWithMnemonic.update(SSVS, "L", "LOCK"); 	
		MNameWithMnemonic.update(SSVS, "R", "ROUTINE"); 	
		MNameWithMnemonic.update(SSVS, "S", "SYSTEM"); 	
		MNameWithMnemonic.update(SSVS, "W", "WINDOW"); 	
	}
	
	private static class MSSVNKeyword extends MKeyWord {
		private static final long serialVersionUID = 1L;

		public MSSVNKeyword(TextPiece p) {
			super(p);
		}
		
		@Override
		public KeywordRefactorFlags getKeywordFlags(MRefactorSettings settings) {
			return settings.ssvnNameSettings;
		}

		@Override
		public MNameWithMnemonic getNameWithMnemonic(String name) {
			return SSVS.get(name);
		}		
	}
	
	public MSsvn(int length) {
		super(length);
	}
	
	public MSsvn(SequenceOfTokens<MToken> tokens) {
		super(tokens);
		TextPiece name = this.getToken(1).toValue();
		MSSVNKeyword newToken = new MSSVNKeyword(name);
		this.setToken(1, newToken);
	}
	
	@Override
	public Node getNode() {
		TextPiece name = this.getToken(1).toValue();
		Tokens<MToken> subsripts = this.getTokens(2);
		NodeList<Node> nodes = NodeUtilities.getSubscriptNodes(subsripts);
		return new StructuredSystemVariable(name, nodes);
	}
}
