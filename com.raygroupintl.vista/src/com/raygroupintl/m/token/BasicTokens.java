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
	
	public static class MTFanoutRoutine extends MTString {
		public MTFanoutRoutine(String value) {
			super(value);
		}
	}
	
	public static class MTIndirectFanoutRoutine extends MTSequence {
		public MTIndirectFanoutRoutine(List<Token> tokens) {
			super(tokens);
		}
	}
	
	public static class MTEnvironmentFanoutRoutine extends MTSequence {
		public MTEnvironmentFanoutRoutine(List<Token> tokens) {
			super(tokens);
		}
	}
}
