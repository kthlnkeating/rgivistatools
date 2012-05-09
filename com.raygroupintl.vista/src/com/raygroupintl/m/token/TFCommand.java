package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.TFSequence;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TCharacters;
import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.bnf.TokenStore;
import com.raygroupintl.vista.struct.MError;

public class TFCommand extends TFSequence {
	private Map<String, TCSFactory> commandSpecs = new HashMap<String, TCSFactory>();
	private MTFSupply supply;
	
	public TFCommand( MTFSupply supply) {
		this.supply = supply;
	}
	
	private static abstract class TCommand extends TArray {
		public TCommand(Token[] tokens) {
			super(tokens);
		}
		
		protected abstract String getFullName();
		
		@Override
		public void beautify() {
			TCharacters n = (TCharacters) this.get(0);
			String newName = this.getFullName();
			n.setValue(newName);
			super.beautify();
		}			
	}
	
	private static class TFGenericArgument extends TokenFactory {
		@Override
		public Token tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			int index = fromIndex;
			boolean inQuotes = false;
			while (index < endIndex) {
				char ch = line.charAt(index);
								
				if (ch == '"') {
					inQuotes = ! inQuotes;
				} else if (ch == ' ') {
					if (! inQuotes) break;
				} else if ((ch == '\r') || (ch == '\n')) {
					break;
				}
				++index;
			}
			if (index > fromIndex) {
				return new TCharacters(line.substring(fromIndex, index));
			} else {
				return new TEmpty();
			}
		}
	}

	private static final TFEmptyVerified TF_EMPTY = TFEmptyVerified.getInstance(' ');
	
	private static abstract class TCommandSpec extends TCharacters {
		private TokenFactory argumentFactory;
		
		public TCommandSpec(String value, TokenFactory argumentFactory) {
			super(value);
			this.argumentFactory = argumentFactory;
		}

		public TokenFactory getArgumentFactory() {
			return this.argumentFactory;
		}

		public abstract Token getToken(Token[] tokens);
	}
		
	private static class TCommandB extends TCommand {
		public TCommandB(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
		
	private static class TBCommandSpec extends TCommandSpec {
		private TBCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandB(tokens);
		}
	}
	
	private static class TCommandC extends TCommand {
		public TCommandC(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}			
	}
		
	private static class TCCommandSpec extends TCommandSpec {
		private TCCommandSpec(String value, MTFSupply supply) {
			super(value, supply.closearg);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandC(tokens);
		}
	}
	
	private static class TCommandD extends TCommand {
		public TCommandD(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "DO";
		}			
	}

	private static class TDCommandSpec extends TCommandSpec {
		private TDCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmddargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandD(tokens);
		}
	}
	
	private static class TCommandE extends TCommand {
		public TCommandE(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
	}

	private static class TECommandSpec extends TCommandSpec {
		private TECommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandE(tokens);
		}
	}

	private static class TCommandF extends TCommand {
		public TCommandF(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "FOR";
		}			
	}

	private static class TFCommandSpec extends TCommandSpec {
		private TFCommandSpec(String value, MTFSupply supply) {
			super(value, supply.forarg);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandF(tokens);
		}
	}

	private static class TCommandG extends TCommand {
		public TCommandG(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "GOTO";
		}			
	}

	private static class TGCommandSpec extends TCommandSpec {
		private TGCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdgargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandG(tokens);
		}
	}

	private static class TCommandH extends TCommand {
		public TCommandH(Token[] tokens) {
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

	private static class THCommandSpec extends TCommandSpec {
		private THCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandH(tokens);
		}
	}

	private static class TCommandI extends TCommand {
		public TCommandI(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "IF";
		}			
	}

	private static class TICommandSpec extends TCommandSpec {
		private TICommandSpec(String value, MTFSupply supply) {
			super(value, supply.exprlist);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandI(tokens);
		}
	}

	private static class TCommandJ extends TCommand {
		public TCommandJ(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	private static class TJCommandSpec extends TCommandSpec {
		private TJCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdjargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandJ(tokens);
		}
	}

	private static class TCommandK extends TCommand {
		public TCommandK(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "KILL";
		}			
	}

	private static class TKCommandSpec extends TCommandSpec {
		private TKCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdkargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandK(tokens);
		}
	}

	private static class TCommandL extends TCommand {
		public TCommandL(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	private static class TLCommandSpec extends TCommandSpec {
		private TLCommandSpec(String value, MTFSupply supply) {
			super(value, supply.lockargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandL(tokens);
		}
	}

	private static class TCommandM extends TCommand {
		public TCommandM(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "MERGE";
		}			
	}

	private static class TMCommandSpec extends TCommandSpec {
		private TMCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdmargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandM(tokens);
		}
	}

	private static class TCommandN extends TCommand {
		public TCommandN(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "NEW";
		}			
	}

	private static class TNCommandSpec extends TCommandSpec {
		private TNCommandSpec(String value, MTFSupply supply) {
			super(value, supply.newargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandN(tokens);
		}
	}

	private static class TCommandO extends TCommand {
		public TCommandO(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "OPEN";
		}			
	}

	private static class TOCommandSpec extends TCommandSpec {
		private TOCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdoargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandO(tokens);
		}
	}

	private static class TCommandQ extends TCommand {
		public TCommandQ(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			
	}

	private static class TQCommandSpec extends TCommandSpec {
		private TQCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandQ(tokens);
		}
	}

	private static class TCommandR extends TCommand {
		public TCommandR(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "READ";
		}			
	}

	private static class TRCommandSpec extends TCommandSpec {
		private TRCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdrargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandR(tokens);
		}
	}

	private static class TCommandS extends TCommand {
		public TCommandS(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "SET";
		}			
	}

	private static class TSCommandSpec extends TCommandSpec {
		private TSCommandSpec(String value, MTFSupply supply) {
			super(value, supply.setargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandS(tokens);
		}
	}

	private static class TCommandTC extends TCommand {
		public TCommandTC(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	private static class TTCCommandSpec extends TCommandSpec {
		private TTCCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandTC(tokens);
		}
	}

	private static class TCommandTR extends TCommand {
		public TCommandTR(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	private static class TTRCommandSpec extends TCommandSpec {
		private TTRCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandTR(tokens);
		}
	}

	private static class TCommandTRO extends TCommand {
		public TCommandTRO(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	private static class TTROCommandSpec extends TCommandSpec {
		private TTROCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandTRO(tokens);
		}
	}

	private static class TCommandTS extends TCommand {
		public TCommandTS(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	private static class TTSCommandSpec extends TCommandSpec {
		private TTSCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandTS(tokens);
		}
	}

	private static class TCommandU extends TCommand {
		public TCommandU(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "USE";
		}			
	}

	private static class TUCommandSpec extends TCommandSpec {
		private TUCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmduargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandU(tokens);
		}
	}

	private static class TCommandW extends TCommand {
		public TCommandW(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			
	}

	private static class TWCommandSpec extends TCommandSpec {
		private TWCommandSpec(String value, MTFSupply supply) {
			super(value, supply.writeargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandW(tokens);
		}
	}

	private static class TCommandV extends TCommand {
		public TCommandV(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	private static class TVCommandSpec extends TCommandSpec {
		private TVCommandSpec(String value, MTFSupply supply) {
			super(value, new TFGenericArgument());
		}
		
		public Token getToken(Token[] tokens) {
			return new TCommandV(tokens);
		}
	}

	private static class TCommandX extends TCommand {
		public TCommandX(Token[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			
	}

	private static class TXCommandSpec extends TCommandSpec {
		private TXCommandSpec(String value, MTFSupply supply) {
			super(value, supply.xecuteargs);
		}
	
		public Token getToken(Token[] tokens) {
			return new TCommandX(tokens);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private TGenericCommandSpec(String value, MTFSupply supply) {
			super(value, new TFGenericArgument());
		}
	
		public Token getToken(Token[] tokens) {
			return new TArray(tokens);
		}
	}
	
	private static abstract class TCSFactory {
		public abstract TCommandSpec get(String name);
	}
	
	public void addCommands(final MTFSupply supply) {
		TCSFactory b = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TBCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("B", b);
		this.commandSpecs.put("BREAK", b); 	
		
		TCSFactory c = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("C", c);
		this.commandSpecs.put("CLOSE", c); 	
		
		TCSFactory d = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TDCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("D", d);
		this.commandSpecs.put("DO", d); 	
		
		TCSFactory e = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TECommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("E", e);
		this.commandSpecs.put("ELSE", e); 	

		TCSFactory f = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TFCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("F", f);
		this.commandSpecs.put("FOR", f); 	
		
		TCSFactory g = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TGCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("G", g);
		this.commandSpecs.put("GOTO", g); 	
		
		TCSFactory h = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new THCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("H", h);
		this.commandSpecs.put("HALT", h); 	
		this.commandSpecs.put("HANG", h); 
		
		TCSFactory i = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TICommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("I", i);
		this.commandSpecs.put("IF", i); 	
		
		TCSFactory j = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TJCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("J", j);
		this.commandSpecs.put("JOB", j); 	
		
		TCSFactory k = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TKCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("K", k);
		this.commandSpecs.put("KILL", k); 	
		
		TCSFactory l = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TLCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("L", l);
		this.commandSpecs.put("LOCK", l); 	
		
		TCSFactory m = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TMCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("M", m);
		this.commandSpecs.put("MERGE", m); 
		
		TCSFactory n = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TNCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("N", n);
		this.commandSpecs.put("NEW", n);		
		
		TCSFactory o = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TOCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("O", o);
		this.commandSpecs.put("OPEN", o); 	
		
		TCSFactory q = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TQCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("Q", q);
		this.commandSpecs.put("QUIT", q); 	
		
		TCSFactory r = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("R", r);
		this.commandSpecs.put("READ", r); 	
		
		TCSFactory s = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("S", s);
		this.commandSpecs.put("SET", s); 	
		
		TCSFactory tc = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TC", tc);
		this.commandSpecs.put("TCOMMIT", tc); 	
		
		TCSFactory tr = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TR", tr);
		this.commandSpecs.put("TRESTART", tr); 	
		
		TCSFactory tro = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTROCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TRO", tro);
		this.commandSpecs.put("TROLLBACK", tro); 	
		
		TCSFactory ts = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TS", ts);
		this.commandSpecs.put("TSTART", ts); 	
		
		TCSFactory u = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TUCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("U", u);
		this.commandSpecs.put("USE", u);
		
		TCSFactory v = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TVCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("V", v);
		this.commandSpecs.put("VIEW", v); 	
		
		TCSFactory w = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TWCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("W", w);
		this.commandSpecs.put("WRITE", w);	
		
		TCSFactory x = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TXCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("X", x);
		this.commandSpecs.put("XECUTE", x);		
	}
	
	public void addCommand(String name, final MTFSupply supply) {
		TCSFactory generic = new TCSFactory() {		
			@Override
			public TCommandSpec get(String name) {
				return new TGenericCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put(name, generic);
	}
			
	private class TFCommandName extends TFIdent {
		@Override
		public Token tokenize(String line, int fromIndex) {
			Token result = super.tokenize(line, fromIndex);
			String cmdName = result.getStringValue();
			TCSFactory tcs = TFCommand.this.commandSpecs.get(cmdName.toUpperCase());
			if (tcs != null) {
				return tcs.get(cmdName);
			} else {
				return new TSyntaxError(MError.ERR_UNDEFINED_COMMAND , line, fromIndex);
			}			
		}
	}
		
	@Override
	protected int getExpectedTokenCount() {
		return 5;
	}
	
	@Override
	protected TokenFactory getTokenFactory(int i, TokenStore foundTokens) {
		switch (i) {
			case 0:
				return TFCommand.this.new TFCommandName();
			case 1:
				return TFCommand.this.supply.postcondition;
			case 2:
				return TFConstChar.getInstance(' ');
			case 3: {
				TCommandSpec cmd = (TCommandSpec) foundTokens.get(0);
				TokenFactory f = cmd.getArgumentFactory();
				return f;
			}					
			case 4:
				return TFBasic.getInstance(' ');
			default:
				assert(i == 5);
				return null;
		}
	}	

	@Override
	protected ValidateResult validateNull(int seqIndex, int lineIndex, TokenStore foundTokens) {
		if (seqIndex == 0) {
			return ValidateResult.NULL_RESULT;
		} else {
			return ValidateResult.CONTINUE;				
		}
	}

	@Override
	public Token getToken(String line, int fromIndex, TokenStore tokens) {
		Token token0 = tokens.get(0);
		if (token0 instanceof TCommandSpec) {
			TCommandSpec spec = (TCommandSpec) token0;
			return spec.getToken(tokens.toArray());
		} else {
			return token0;
		}
	}

	public static TFCommand getInstance(MTFSupply supply) {
		return new TFCommand(supply);
	}
}
