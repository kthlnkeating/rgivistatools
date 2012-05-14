package com.raygroupintl.m.token;

import com.raygroupintl.bnf.TString;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.m.parsetree.GenericCommand;
import com.raygroupintl.m.parsetree.Node;

class TCommand {
	private static abstract class TCommandBase extends MTArray {
		public TCommandBase(Token[] tokens) {
			super(tokens);
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
		public B(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
		
	static class C extends TCommandBase {
		public C(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}			
	}
		
	static class D extends TCommandBase {
		public D(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "DO";
		}			
	}

	static class E extends TCommandBase {
		public E(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
	}

	static class F extends TCommandBase {
		public F(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "FOR";
		}			
	}

	static class G extends TCommandBase {
		public G(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "GOTO";
		}			
	}

	static class H extends TCommandBase {
		public H(Token[] tokens) {
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
		public I(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "IF";
		}			
	}

	static class J extends TCommandBase {
		public J(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	static class K extends TCommandBase {
		public K(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "KILL";
		}			
	}


	static class L extends TCommandBase {
		public L(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	static class M extends TCommandBase {
		public M(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}			
	}

	static class N extends TCommandBase {
		public N(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "NEW";
		}			
	}

	static class O extends TCommandBase {
		public O(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}			
	}


	static class Q extends TCommandBase {
		public Q(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			
	}

	static class R extends TCommandBase {
		public R(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "READ";
		}			
	}

	static class S extends TCommandBase {
		public S(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "SET";
		}			
	}

	static class TC extends TCommandBase {
		public TC(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	static class TR extends TCommandBase {
		public TR(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	static class TRO extends TCommandBase {
		public TRO(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	static class TS extends TCommandBase {
		public TS(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	static class U extends TCommandBase {
		public U(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}			
	}

	static class W extends TCommandBase {
		public W(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			
	}

	static class V extends TCommandBase {
		public V(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	static class X extends TCommandBase {
		public X(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			
	}

	static class Generic extends TCommandBase {
		public Generic(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return this.getStringValue();
		}					
	}
}

