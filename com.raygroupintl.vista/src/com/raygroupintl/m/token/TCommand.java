package com.raygroupintl.m.token;

import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Goto;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.TEmpty;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Token;

class TCommand {
	private static abstract class TCommandBase extends MTSequence {
		public TCommandBase(Token token) {
			super(token);
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
			if ((argument == null) || (argument.toValue().length() == 0)) {
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
			StringPiece newName = new StringPiece(getFullName());
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
		public B(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
		
	static class C extends TCommandBase {
		public C(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}			
	}
		
	static class D extends TCommandBase {
		public D(Token token) {
			super(token);
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
		public E(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
	}

	static class F extends TCommandBase {
		public F(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "FOR";
		}			
	}

	static class G extends TCommandBase {
		public G(Token token) {
			super(token);
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
		public H(Token token) {
			super(token);
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
		public I(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "IF";
		}			
	}

	static class J extends TCommandBase {
		public J(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	static class K extends TCommandBase {
		public K(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "KILL";
		}			
	}


	static class L extends TCommandBase {
		public L(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	static class M extends TCommandBase {
		public M(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}			
	}

	static class N extends TCommandBase {
		public N(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "NEW";
		}			
	}

	static class O extends TCommandBase {
		public O(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}			
	}


	static class Q extends TCommandBase {
		public Q(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			
	}

	static class R extends TCommandBase {
		public R(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "READ";
		}			
	}

	static class S extends TCommandBase {
		public S(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "SET";
		}			
	}

	static class TC extends TCommandBase {
		public TC(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	static class TR extends TCommandBase {
		public TR(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	static class TRO extends TCommandBase {
		public TRO(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	static class TS extends TCommandBase {
		public TS(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	static class U extends TCommandBase {
		public U(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}			
	}

	static class W extends TCommandBase {
		public W(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			
	}

	static class V extends TCommandBase {
		public V(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	static class X extends TCommandBase {
		public X(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			
	}

	static class Generic extends TCommandBase {
		public Generic(Token token) {
			super(token);
		}		
		
		@Override
		protected String getFullName() {		
			return this.toValue().toString();
		}					
	}
}

