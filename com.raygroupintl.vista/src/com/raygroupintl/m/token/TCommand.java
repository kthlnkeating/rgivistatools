package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.bnf.TSequence;
import com.raygroupintl.bnf.TString;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Node;

class TCommand {
	private static abstract class TCommandBase extends MTSequence {
		public TCommandBase(List<Token> tokens) {
			super(tokens);
		}
		
		protected Token getArguments() {
			TSequence nameFollowUp = (TSequence) this.get(1);
			if (nameFollowUp == null) {
				return null;
			}
			Token arguments = nameFollowUp.get(2);
			return arguments;
		}
	
		protected Token getPostCondition() {
			TSequence nameFollowUp = (TSequence) this.get(1);
			if (nameFollowUp == null) {
				return null;
			}
			TSequence postConditionWithColon = (TSequence) nameFollowUp.get(0);
			if (postConditionWithColon == null) {
				return null;
			} else {
				return postConditionWithColon.get(1);
			}
		}
	
		protected abstract String getFullName();
	
		@Override
		public void beautify() {
			TString n = (TString) this.get(0);
			String newName = this.getFullName();
			n.setValue(newName);
			super.beautify();
		}
		
		@Override
		public Node getNode() {
			return new GenericCommand(null, null);
		}
	}
	
	static class B extends TCommandBase {
		public B(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
		
	static class C extends TCommandBase {
		public C(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}			
	}
		
	static class D extends TCommandBase {
		public D(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "DO";
		}			

		@Override
		public Node getNode() {
			Token argument = (Token) this.getArguments();
			if ((argument == null) || (argument.getStringSize() == 0)) {
				return new DoBlock();
			} else {
				Token postCondition = this.getPostCondition(); 
				if ((postCondition == null) || ! (postCondition instanceof MToken)) {
					return new Do(null);
				} else {
					Node postConditionNode = ((MToken) postCondition).getNode();
					Do result = new Do(postConditionNode);
					return result;
				}
			}
		}
	}

	static class E extends TCommandBase {
		public E(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
	}

	static class F extends TCommandBase {
		public F(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "FOR";
		}			
	}

	static class G extends TCommandBase {
		public G(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "GOTO";
		}			
	}

	static class H extends TCommandBase {
		public H(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {
			Token argument = this.get(3);
			if (argument == null) {
				return "HALT";
			} else {
				return "HANG";
			}
		}			
	}

	static class I extends TCommandBase {
		public I(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "IF";
		}			
	}

	static class J extends TCommandBase {
		public J(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	static class K extends TCommandBase {
		public K(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "KILL";
		}			
	}


	static class L extends TCommandBase {
		public L(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	static class M extends TCommandBase {
		public M(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}			
	}

	static class N extends TCommandBase {
		public N(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "NEW";
		}			
	}

	static class O extends TCommandBase {
		public O(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}			
	}


	static class Q extends TCommandBase {
		public Q(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			
	}

	static class R extends TCommandBase {
		public R(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "READ";
		}			
	}

	static class S extends TCommandBase {
		public S(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "SET";
		}			
	}

	static class TC extends TCommandBase {
		public TC(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	static class TR extends TCommandBase {
		public TR(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	static class TRO extends TCommandBase {
		public TRO(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	static class TS extends TCommandBase {
		public TS(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	static class U extends TCommandBase {
		public U(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}			
	}

	static class W extends TCommandBase {
		public W(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			
	}

	static class V extends TCommandBase {
		public V(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	static class X extends TCommandBase {
		public X(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			
	}

	static class Generic extends TCommandBase {
		public Generic(List<Token> tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return this.getStringValue();
		}					
	}
}

