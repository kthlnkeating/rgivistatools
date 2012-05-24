package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.parser.Token;

public class BasicTokens {
	public static class MTFanoutTag extends MTString {
		public MTFanoutTag(String value) {
			super(value);
		}
	}
	
	public static class MTIndirectFanoutTag extends MTSequence {
		public MTIndirectFanoutTag(List<Token> tokens) {
			super(tokens);
		}
	}
	
	public static class MTFanoutTagOffset extends MTSequence {
		public MTFanoutTagOffset(List<Token> tokens) {
			super(tokens);
		}
	}
	
	public static class MTFanoutRoutine extends MTSequence {
		public MTFanoutRoutine(List<Token> tokens) {
			super(tokens);
		}
	}
	
	public static class MTIndirectFanoutRoutine extends TIndirection {
		public MTIndirectFanoutRoutine(List<Token> tokens) {
			super(tokens);
		}

		//@Override
		//public Node getNode() {
		//	Nodes nodes = NodeUtilities.getNodes(this);
		//	Indirection result = new Indirection(nodes);
		//	return result;
		//}		
	}
	
	public static class MTEnvironmentFanoutRoutine extends MTSequence {
		public MTEnvironmentFanoutRoutine(List<Token> tokens) {
			super(tokens);
		}
	}
}
