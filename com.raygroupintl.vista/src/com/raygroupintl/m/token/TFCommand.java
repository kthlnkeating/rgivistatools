package com.raygroupintl.m.token;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.StringAdapter;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TEmpty;
import com.raygroupintl.parser.TFEmptyVerified;
import com.raygroupintl.parser.TFSequenceStatic;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenFactorySupply;
import com.raygroupintl.parser.TokenStore;

public class TFCommand extends TokenFactorySupply {
	private Map<String, TCSFactory> commandSpecs = new HashMap<String, TCSFactory>();
	private MTFSupply supply;
	private MAdapterSupply adapterSupply = new MAdapterSupply();
	
	public TFCommand(String name, MTFSupply supply) {
		super(name);
		this.supply = supply;
	}
	
	private static class CommandNameAdapter implements StringAdapter {
		@Override
		public Token convert(String value) {
			return new TString(value);
		}
	}
	
	private static class TFGenericArgument extends TokenFactory {
		public TFGenericArgument(String name) {
			super(name);
		}
		
		@Override
		public Token tokenize(Text text) {
			int index = 0;
			boolean inQuotes = false;
			while (text.onChar(index)) {
				char ch = text.getChar(index);
								
				if (ch == '"') {
					inQuotes = ! inQuotes;
				} else if (ch == ' ') {
					if (! inQuotes) break;
				} else if ((ch == '\r') || (ch == '\n')) {
					break;
				}
				++index;
			}
			if (index > 0) {
				return text.extractToken(index, new CommandNameAdapter());
			} else {
				return new TEmpty();
			}
		}
	}

	private static final TFEmptyVerified TF_EMPTY = TFEmptyVerified.getInstance("commandempty", ' ');
	
	private static abstract class TCommandSpec extends MTString {
		private TokenFactory argumentFactory;
		
		public TCommandSpec(String value, TokenFactory argumentFactory) {
			super(value);
			this.argumentFactory = argumentFactory;
		}

		public TokenFactory getArgumentFactory() {
			return this.argumentFactory;
		}

		public abstract Token getToken(List<Token> tokens);
	}
		
	private static class TBCommandSpec extends TCommandSpec {
		private TBCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.B(tokens);
		}
	}
	
	private static class TCCommandSpec extends TCommandSpec {
		private TCCommandSpec(String value, MTFSupply supply) {
			super(value, supply.closearg);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.C(tokens);
		}
	}
	
	private static class TDCommandSpec extends TCommandSpec {
		private TDCommandSpec(String value, MTFSupply supply) {
			super(value, supply.doarguments);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.D(tokens);
		}
	}
	
	private static class TECommandSpec extends TCommandSpec {
		private TECommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.E(tokens);
		}
	}

	private static class TFCommandSpec extends TCommandSpec {
		private TFCommandSpec(String value, MTFSupply supply) {
			super(value, supply.forarg);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.F(tokens);
		}
	}

	private static class TGCommandSpec extends TCommandSpec {
		private TGCommandSpec(String value, MTFSupply supply) {
			super(value, supply.gotoarguments);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.G(tokens);
		}
	}

	private static class THCommandSpec extends TCommandSpec {
		private THCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.H(tokens);
		}
	}

	private static class TICommandSpec extends TCommandSpec {
		private TICommandSpec(String value, MTFSupply supply) {
			super(value, supply.exprlist);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.I(tokens);
		}
	}

	private static class TJCommandSpec extends TCommandSpec {
		private TJCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdjargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.J(tokens);
		}
	}

	private static class TKCommandSpec extends TCommandSpec {
		private TKCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdkargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.K(tokens);
		}
	}

	private static class TLCommandSpec extends TCommandSpec {
		private TLCommandSpec(String value, MTFSupply supply) {
			super(value, supply.lockargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.L(tokens);
		}
	}

	private static class TMCommandSpec extends TCommandSpec {
		private TMCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdmargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.M(tokens);
		}
	}

	private static class TNCommandSpec extends TCommandSpec {
		private TNCommandSpec(String value, MTFSupply supply) {
			super(value, supply.newargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.N(tokens);
		}
	}

	private static class TOCommandSpec extends TCommandSpec {
		private TOCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdoargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.O(tokens);
		}
	}

	private static class TQCommandSpec extends TCommandSpec {
		private TQCommandSpec(String value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.Q(tokens);
		}
	}

	private static class TRCommandSpec extends TCommandSpec {
		private TRCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmdrargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.R(tokens);
		}
	}

	private static class TSCommandSpec extends TCommandSpec {
		private TSCommandSpec(String value, MTFSupply supply) {
			super(value, supply.setargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.S(tokens);
		}
	}

	private static class TTCCommandSpec extends TCommandSpec {
		private TTCCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.TC(tokens);
		}
	}

	private static class TTRCommandSpec extends TCommandSpec {
		private TTRCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.TR(tokens);
		}
	}

	private static class TTROCommandSpec extends TCommandSpec {
		private TTROCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.TRO(tokens);
		}
	}

	private static class TTSCommandSpec extends TCommandSpec {
		private TTSCommandSpec(String value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.TS(tokens);
		}
	}

	private static class TUCommandSpec extends TCommandSpec {
		private TUCommandSpec(String value, MTFSupply supply) {
			super(value, supply.cmduargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.U(tokens);
		}
	}

	private static class TWCommandSpec extends TCommandSpec {
		private TWCommandSpec(String value, MTFSupply supply) {
			super(value, supply.writeargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.W(tokens);
		}
	}

	private static class TVCommandSpec extends TCommandSpec {
		private TVCommandSpec(String value, MTFSupply supply) {
			super(value, new TFGenericArgument("vargument"));
		}
		
		public Token getToken(List<Token> tokens) {
			return new TCommand.V(tokens);
		}
	}

	private static class TXCommandSpec extends TCommandSpec {
		private TXCommandSpec(String value, MTFSupply supply) {
			super(value, supply.xecuteargs);
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.X(tokens);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private TGenericCommandSpec(String value, MTFSupply supply) {
			super(value, new TFGenericArgument("genericargument"));
		}
	
		public Token getToken(List<Token> tokens) {
			return new TCommand.Generic(tokens);
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
	
	@Override
	public TokenFactory getSupplyTokenFactory() {
		return this.new TFCommandName("command.name", this.supply.ident);
	}
	
	private class TFCommandRest extends TFSequenceStatic {
		public TFCommandRest(String name, TokenFactory... factories) {
			super(name, TFCommand.this.adapterSupply.getSequenceAdapter(), factories);
		}
		
		@Override
		protected ValidateResult validateNull(int seqIndex, TokenStore foundTokens) throws SyntaxErrorException {
			if (seqIndex == 3) {
				throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX, foundTokens);				
			} else {
				return ValidateResult.CONTINUE;
			}
		}
	}

	private class TFCommandName extends TokenFactory {
		private TokenFactory slave;
		
		public TFCommandName(String name, TokenFactory slave) {
			super(name);
			this.slave = slave;
		}
		
		@Override
		public Token tokenize(Text text) throws SyntaxErrorException {
			Token token = this.slave.tokenize(text);
			if (token == null) {
				return null;
			} else {
				String cmdName = token.getStringValue();
				TCSFactory tcs = TFCommand.this.commandSpecs.get(cmdName.toUpperCase());
				if (tcs == null) {
					throw new SyntaxErrorException(MError.ERR_UNDEFINED_COMMAND);					
				} else {
					TCommandSpec spec = tcs.get(cmdName);
					return spec;
				}
			}
		}
	}
	
	@Override
	public TokenFactory getNextTokenFactory(Token token) throws SyntaxErrorException {
		TCommandSpec spec = (TCommandSpec) token;
		TokenFactory argumentFactory = spec.getArgumentFactory();
		TFSequenceStatic tf = new TFCommandRest(this.getName(), this.supply.postcondition, this.supply.space, argumentFactory, this.supply.commandend);
		tf.setRequiredFlags(false, false, false, false);
		return tf;
	}

		
	@Override
	public Token tokenize(Text text) {
		Text textCopy = text.getCopy();
		try {
			return super.tokenize(text);
		} catch (SyntaxErrorException e) {
			int errorIndex = text.getIndex();
			TSyntaxError.Adapter adapter = new TSyntaxError.Adapter(e.getCode(), errorIndex);
			int lengthToEOL = textCopy.findEOL();
			Token t = textCopy.extractToken(lengthToEOL, adapter);
			text.copyFrom(textCopy);
			return t;
		}
	}
	
	
	@Override
	public Token getToken(Token supplyToken, Token nextToken) {
		String cmdName = supplyToken.getStringValue();
		TCSFactory tcs = this.commandSpecs.get(cmdName.toUpperCase());
		TCommandSpec spec = tcs.get(cmdName);
		return spec.getToken(Arrays.asList(new Token[]{supplyToken, nextToken}));
	}
}
