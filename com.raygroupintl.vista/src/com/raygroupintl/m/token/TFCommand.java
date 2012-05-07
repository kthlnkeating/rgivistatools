package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.TokenFactorySupply;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TBasic;
import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.vista.struct.MError;

public class TFCommand extends TFSeq {
	private MVersion version;
	
	private TFCommand(MVersion version) {
		this.version = version;
	}
	
	private static abstract class TCommand extends TArray {
		public TCommand(Token[] tokens) {
			super(tokens);
		}
		
		protected abstract String getFullName();
		
		@Override
		public void beautify() {
			TBasic n = (TBasic) this.get(0);
			String newName = this.getFullName();
			n.setValue(newName);
			super.beautify();
		}			
	}
	
	private static class TFGenericArgument implements TokenFactory {
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
				return new TBasic(line.substring(fromIndex, index));
			} else {
				return new TEmpty();
			}
		}
	}

	private static final TFEmptyVerified TF_EMPTY = TFEmptyVerified.getInstance(' ');
	
	private static abstract class TCommandSpec extends TBasic {
		private TokenFactory argumentFactory;
		
		public TCommandSpec(String value) {
			super(value);
		}

		public TokenFactory getArgumentFactory(final MVersion version) {
			if (this.argumentFactory == null) {
				this.argumentFactory = this.buildArgumentFactory(version);
			}
			return this.argumentFactory;
		}

		protected abstract TokenFactory buildArgumentFactory(final MVersion version);	
		
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
		private TBCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr;
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
		private TCCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).closeargs;		
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
		private TDCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmddargs;
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
		private TECommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
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
		private TFCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).forarg;
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
		private TGCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdgargs;	
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
		private THCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr;
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
		private TICommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).exprlist;	
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
		private TJCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdjargs;
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
		private TKCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdkargs;			
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
		private TLCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).lockargs;
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
		private TMCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdmargs; 	
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
		private TNCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).newargs;
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
		private TOCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdoargs;	
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
		private TQCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr; 	
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
		private TRCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdrargs;
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
		private TSCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).setargs;
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
		private TTCCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
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
		private TTRCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
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
		private TTROCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
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
		private TTSCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
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
		private TUCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmduargs;
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
		private TWCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).writeargs;			
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
		private TVCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return new TFGenericArgument();
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
		private TXCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).xecuteargs;
		}
		
		public Token getToken(Token[] tokens) {
			return new TCommandX(tokens);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private TGenericCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected TokenFactory buildArgumentFactory(final MVersion version) {
			return new TFGenericArgument();
		}
		
		public Token getToken(Token[] tokens) {
			return new TArray(tokens);
		}
	}
	
	private static abstract class TCSFactory {
		public abstract TCommandSpec get(String name);
	}
	
	private static Map<String, TCSFactory> COMMAND_SPECS = new HashMap<String, TCSFactory>();
	static {
		TCSFactory b = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TBCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("B", b);
		COMMAND_SPECS.put("BREAK", b); 	
		
		TCSFactory c = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TCCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("C", c);
		COMMAND_SPECS.put("CLOSE", c); 	
		
		TCSFactory d = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TDCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("D", d);
		COMMAND_SPECS.put("DO", d); 	
		
		TCSFactory e = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TECommandSpec(name);
			}
		};
		COMMAND_SPECS.put("E", e);
		COMMAND_SPECS.put("ELSE", e); 	

		TCSFactory f = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TFCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("F", f);
		COMMAND_SPECS.put("FOR", f); 	
		
		TCSFactory g = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TGCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("G", g);
		COMMAND_SPECS.put("GOTO", g); 	
		
		TCSFactory h = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new THCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("H", h);
		COMMAND_SPECS.put("HALT", h); 	
		COMMAND_SPECS.put("HANG", h); 
		
		TCSFactory i = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TICommandSpec(name);
			}
		};
		COMMAND_SPECS.put("I", i);
		COMMAND_SPECS.put("IF", i); 	
		
		TCSFactory j = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TJCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("J", j);
		COMMAND_SPECS.put("JOB", j); 	
		
		TCSFactory k = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TKCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("K", k);
		COMMAND_SPECS.put("KILL", k); 	
		
		TCSFactory l = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TLCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("L", l);
		COMMAND_SPECS.put("LOCK", l); 	
		
		TCSFactory m = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TMCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("M", m);
		COMMAND_SPECS.put("MERGE", m); 
		
		TCSFactory n = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TNCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("N", n);
		COMMAND_SPECS.put("NEW", n);		
		
		TCSFactory o = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TOCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("O", o);
		COMMAND_SPECS.put("OPEN", o); 	
		
		TCSFactory q = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TQCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("Q", q);
		COMMAND_SPECS.put("QUIT", q); 	
		
		TCSFactory r = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TRCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("R", r);
		COMMAND_SPECS.put("READ", r); 	
		
		TCSFactory s = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TSCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("S", s);
		COMMAND_SPECS.put("SET", s); 	
		
		TCSFactory tc = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTCCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("TC", tc);
		COMMAND_SPECS.put("TCOMMIT", tc); 	
		
		TCSFactory tr = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTRCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("TR", tr);
		COMMAND_SPECS.put("TRESTART", tr); 	
		
		TCSFactory tro = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTROCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("TRO", tro);
		COMMAND_SPECS.put("TROLLBACK", tro); 	
		
		TCSFactory ts = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TTSCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("TS", ts);
		COMMAND_SPECS.put("TSTART", ts); 	
		
		TCSFactory u = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TUCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("U", u);
		COMMAND_SPECS.put("USE", u);
		
		TCSFactory v = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TVCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("V", v);
		COMMAND_SPECS.put("VIEW", v); 	
		
		TCSFactory w = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TWCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("W", w);
		COMMAND_SPECS.put("WRITE", w);	
		
		TCSFactory x = new TCSFactory() {			
			@Override
			public TCommandSpec get(String name) {
				return new TXCommandSpec(name);
			}
		};
		COMMAND_SPECS.put("X", x);
		COMMAND_SPECS.put("XECUTE", x);		
	}
	
	private static final TCSFactory GENERIC_TCS_FACTORY = new TCSFactory() {		
		@Override
		public TCommandSpec get(String name) {
			return new TGenericCommandSpec(name);
		}
	};
	
	public static void addCommand(String name) {
		COMMAND_SPECS.put(name, GENERIC_TCS_FACTORY);
	}
			
	private static class TFCommandName extends TFIdent {
		@Override
		public Token tokenize(String line, int fromIndex) {
			Token result = super.tokenize(line, fromIndex);
			String cmdName = result.getStringValue();
			TCSFactory tcs = COMMAND_SPECS.get(cmdName.toUpperCase());
			if (tcs != null) {
				return tcs.get(cmdName);
			} else {
				return new TSyntaxError(MError.ERR_UNDEFINED_COMMAND , line, fromIndex);
			}			
		}
		
		public static TFCommandName getInstance() {
			return new TFCommandName();
		}
	}
		
	private static class TFSCommand implements TokenFactorySupply {
		private MVersion version;
		
		private TFSCommand(MVersion version) {
			this.version = version;
		}
			
		public TokenFactory get(int seqIndex, Token[] previousTokens) {
			switch (seqIndex) {
				case 0:
					return TFCommandName.getInstance();
				case 1:
					return MTFSupply.getInstance(version).postcondition;
				case 2:
					return TFConstChar.getInstance(' ');
				case 3: {
					TCommandSpec cmd = (TCommandSpec) previousTokens[0];
					TokenFactory f = cmd.getArgumentFactory(this.version);
					return f;
				}					
				case 4:
					return TFBasic.getInstance(' ');
				default:
					assert(seqIndex == 5);
					return null;
			}
		}
		
		public int getCount() {
			return 5;
		}
	}
	
	@Override
	public Token tokenize(String line, int fromIndex) throws SyntaxErrorException {
		return super.tokenize(line, fromIndex);
	}
	
	@Override
	protected TokenFactorySupply getFactorySupply() {
		return new TFSCommand(this.version);
	}

	@Override
	protected int validateNull(int seqIndex, Token[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}
		return CONTINUE;				
	}

	@Override
	protected int validateEnd(int seqIndex, Token[] foundTokens) {
		return 0;
	}

	@Override
	public Token getToken(String line, int fromIndex, Token[] tokens) {
		TCommandSpec spec = (TCommandSpec) tokens[0];
		return spec.getToken(tokens);
	}
			
	public static TFCommand getInstance(MVersion version) {
		return new TFCommand(version);
	}
}
