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
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;

public class BasicTokens {
	public static class MTFanoutLabelA extends MSequence {
		public MTFanoutLabelA(int length) {
			super(length);
		}
		
		public MTFanoutLabelA(TokenStore store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			String value = this.toValue().toString();
			return new FanoutLabel(value, addlNode);
		}		
	}
	
	public static class MTFanoutLabelB extends MString {
		private static final long serialVersionUID = 1L;
		
		public MTFanoutLabelB(Token value) {
			super(value);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			StringPiece value = this.toValue();
			return new FanoutLabel(value.toString(), addlNode);
		}		
	}
	
	public static class MTIndirectFanoutLabel extends MIndirection {
		public MTIndirectFanoutLabel(int length) {
			super(length);
		}
		
		public MTIndirectFanoutLabel(TokenStore store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new IndirectFanoutLabel(addlNode);
		}		
	}
	
	public static class MTFanoutRoutine extends MSequence {
		public MTFanoutRoutine(int length) {
			super(length);
		}
		
		public MTFanoutRoutine(TokenStore store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			String name = this.toValue().toString();
			Node addlNode = super.getNode();
			return new FanoutRoutine(name, addlNode);
		}		
	}
	
	public static class MTIndirectFanoutRoutine extends MIndirection {
		public MTIndirectFanoutRoutine(int length) {
			super(length);
		}
	
		public MTIndirectFanoutRoutine(TokenStore store) {
			super(store);
		}
	
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new IndirectFanoutRoutine(addlNode);
		}		
	}
	
	public static class MTEnvironmentFanoutRoutine extends MSequence {
		public MTEnvironmentFanoutRoutine(int length) {
			super(length);
		}
		
		public MTEnvironmentFanoutRoutine(TokenStore store) {
			super(store);
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
		
		public MPostCondition(TokenStore store) {
			super(store);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = ((MToken) this.get(1)).getNode();
			return new PostConditional(addlNode);
		}		
	}
}
