package com.raygroupintl.m.token;

import java.util.List;

import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Goto;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.TEmpty;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Token;

class TCommand {
	private static abstract class TCommandBase extends MTSequence {
		public TCommandBase(List<Token> tokens) {
			super(tokens);
		}
		
		protected Node getArgumentNode() {
			MTSequence nameFollowUp = (MTSequence) this.get(1);
			if (nameFollowUp == null) {
				return null;
			}
			if (nameFollowUp.get(2) instanceof TEmpty) {
				return null;
			}			
			MToken argument = (MToken) nameFollowUp.get(2);
			if ((argument == null) || (argument.getStringSize() == 0)) {
				return null;
			} else {				
				return argument.getNode();
			}
		}
	
		protected Node getPostConditionNode() {
			MTSequence nameFollowUp = (MTSequence) this.get(1);
			if (nameFollowUp == null) {
				return null;
			}
			MTSequence postConditionWithColon = (MTSequence) nameFollowUp.get(0);
			if (postConditionWithColon == null) {
				return null;
			} else {
				MToken postConditionToken = (MToken) postConditionWithColon.get(1);
				return postConditionToken.getNode();
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
			Node postConditionNode = this.getPostConditionNode();
			Node argumentNode = this.getArgumentNode();
			GenericCommand result = new GenericCommand(postConditionNode, argumentNode);
			return result;
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
			Node postConditionNode = this.getPostConditionNode();
			Node argumentNode = this.getArgumentNode();
			if (argumentNode == null) {
				return new DoBlock(postConditionNode);
			} else {
				Do result = new Do(postConditionNode, argumentNode);
				return result;
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
		
		@Override
		public Node getNode() {
			Node postConditionNode = this.getPostConditionNode();
			Node argumentNode = this.getArgumentNode();
			Goto result = new Goto(postConditionNode, argumentNode);
			return result;
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

