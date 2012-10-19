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

import com.raygroupintl.m.parsetree.EnvironmentFanoutRoutine;
import com.raygroupintl.m.parsetree.FanoutLabel;
import com.raygroupintl.m.parsetree.FanoutRoutine;
import com.raygroupintl.m.parsetree.IndirectFanoutLabel;
import com.raygroupintl.m.parsetree.IndirectFanoutRoutine;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.PostConditional;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;

public class BasicTokens {
	public static class MTFanoutLabelA extends MTokenCopy {
		public MTFanoutLabelA(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			String value = this.toValue().toString();
			return new FanoutLabel(value, subNode);
		}		
	}
	
	public static class MTFanoutLabelB extends MTokenCopy {
		public MTFanoutLabelB(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			StringPiece value = this.toValue();
			return new FanoutLabel(value.toString(), subNode);
		}		
	}
	
	public static class MTIndirectFanoutLabel extends MTokenCopy {
		public MTIndirectFanoutLabel(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			return new IndirectFanoutLabel(subNode);
		}		
	}
	
	public static class MTFanoutRoutine extends MTokenCopy {
		public MTFanoutRoutine(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode(Node subNode) {
			String name = this.toValue().toString();
			return new FanoutRoutine(name, subNode);
		}		
	}
	
	public static class MTIndirectFanoutRoutine extends MTokenCopy {
		public MTIndirectFanoutRoutine(Token token) {
			super(token);
		}
	
		@Override
		public Node getNode(Node subNode) {
			return new IndirectFanoutRoutine(subNode);
		}		
	}
	
	public static class MTEnvironmentFanoutRoutine extends MSequence {
		public MTEnvironmentFanoutRoutine(int length) {
			super(length);
		}
		
		public MTEnvironmentFanoutRoutine(SequenceOfTokens tokens) {
			super(tokens);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new EnvironmentFanoutRoutine(addlNode);
		}		
	}

	public static class MPostCondition extends MSequence {
		public MPostCondition(int length) {
			super(length);
		}
		
		public MPostCondition(SequenceOfTokens tokens) {
			super(tokens);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = ((MToken) this.getToken(1)).getNode();
			return new PostConditional(addlNode);
		}		
	}
}
