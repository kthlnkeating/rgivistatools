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
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Token;

public class BasicTokens {
	public static class MTFanoutLabelA extends MTSequence {
		public MTFanoutLabelA(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			String value = this.toValue().toString();
			return new FanoutLabel(value, addlNode);
		}		
	}
	
	public static class MTFanoutLabelB extends MTString {
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
	
	public static class MTIndirectFanoutLabel extends TIndirection {
		public MTIndirectFanoutLabel(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new IndirectFanoutLabel(addlNode);
		}		
	}
	
	public static class MTFanoutTagOffset extends MTSequence {
		public MTFanoutTagOffset(Token token) {
			super(token);
		}
	}
	
	public static class MTFanoutRoutine extends MTSequence {
		public MTFanoutRoutine(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			String name = this.toValue().toString();
			Node addlNode = super.getNode();
			return new FanoutRoutine(name, addlNode);
		}		
	}
	
	public static class MTIndirectFanoutRoutine extends TIndirection {
		public MTIndirectFanoutRoutine(Token token) {
			super(token);
		}
	
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new IndirectFanoutRoutine(addlNode);
		}		
	}
	
	public static class MTEnvironmentFanoutRoutine extends MTSequence {
		public MTEnvironmentFanoutRoutine(Token token) {
			super(token);
		}
		
		@Override
		public Node getNode() {
			Node addlNode = super.getNode();
			return new EnvironmentFanoutRoutine(addlNode);
		}		
	}
}
