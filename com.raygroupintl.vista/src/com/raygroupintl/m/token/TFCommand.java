package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFEmptyVerified;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.TSequence;
import com.raygroupintl.parser.TString;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenFactorySupply;
import com.raygroupintl.parser.annotation.ObjectSupply;

public class TFCommand extends TokenFactorySupply {
	private Map<String, TCSFactory> commandSpecs = new HashMap<String, TCSFactory>();
	private MTFSupply supply;
	
	public TFCommand(String name, MTFSupply supply) {
		super(name);
		this.supply = supply;
	}
	
	private static class TFGenericArgument extends TokenFactory {
		public TFGenericArgument(String name) {
			super(name);
		}
		
		@Override
		public Token tokenize(Text text, ObjectSupply objectSupply) {
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
				return text.extractToken(index, objectSupply);
			} else {
				return new MEmpty();
			}
		}
	}

	private static final TFEmptyVerified TF_EMPTY = new TFEmptyVerified("commandempty", ' ');
	
	private static abstract class TCommandSpec extends MString {
		private TokenFactory argumentFactory;
		
		public TCommandSpec(StringPiece value, TokenFactory argumentFactory) {
			super(value);
			this.argumentFactory = argumentFactory;
		}

		public TokenFactory getArgumentFactory() {
			return this.argumentFactory;
		}

		public abstract Token getToken(Token token);
	}
		
	private static class TBCommandSpec extends TCommandSpec {
		private TBCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token token) {
			return new MCommand.B(token);
		}
	}
	
	private static class TCCommandSpec extends TCommandSpec {
		private TCCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.closearg);
		}
	
		public Token getToken(Token token) {
			return new MCommand.C(token);
		}
	}
	
	private static class TDCommandSpec extends TCommandSpec {
		private TDCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.doarguments);
		}
	
		public Token getToken(Token token) {
			return new MCommand.D(token);
		}
	}
	
	private static class TECommandSpec extends TCommandSpec {
		private TECommandSpec(StringPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token token) {
			return new MCommand.E(token);
		}
	}

	private static class TFCommandSpec extends TCommandSpec {
		private TFCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.forarg);
		}
	
		public Token getToken(Token token) {
			return new MCommand.F(token);
		}
	}

	private static class TGCommandSpec extends TCommandSpec {
		private TGCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.gotoarguments);
		}
	
		public Token getToken(Token token) {
			return new MCommand.G(token);
		}
	}

	private static class THCommandSpec extends TCommandSpec {
		private THCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token token) {
			return new MCommand.H(token);
		}
	}

	private static class TICommandSpec extends TCommandSpec {
		private TICommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.exprlist);
		}
	
		public Token getToken(Token token) {
			return new MCommand.I(token);
		}
	}

	private static class TJCommandSpec extends TCommandSpec {
		private TJCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmdjargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.J(token);
		}
	}

	private static class TKCommandSpec extends TCommandSpec {
		private TKCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmdkargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.K(token);
		}
	}

	private static class TLCommandSpec extends TCommandSpec {
		private TLCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.lockargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.L(token);
		}
	}

	private static class TMCommandSpec extends TCommandSpec {
		private TMCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmdmargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.M(token);
		}
	}

	private static class TNCommandSpec extends TCommandSpec {
		private TNCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.newargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.N(token);
		}
	}

	private static class TOCommandSpec extends TCommandSpec {
		private TOCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmdoargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.O(token);
		}
	}

	private static class TQCommandSpec extends TCommandSpec {
		private TQCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public Token getToken(Token token) {
			return new MCommand.Q(token);
		}
	}

	private static class TRCommandSpec extends TCommandSpec {
		private TRCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmdrargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.R(token);
		}
	}

	private static class TSCommandSpec extends TCommandSpec {
		private TSCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.setargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.S(token);
		}
	}

	private static class TTCCommandSpec extends TCommandSpec {
		private TTCCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token token) {
			return new MCommand.TC(token);
		}
	}

	private static class TTRCommandSpec extends TCommandSpec {
		private TTRCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token token) {
			return new MCommand.TR(token);
		}
	}

	private static class TTROCommandSpec extends TCommandSpec {
		private TTROCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token token) {
			return new MCommand.TRO(token);
		}
	}

	private static class TTSCommandSpec extends TCommandSpec {
		private TTSCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public Token getToken(Token token) {
			return new MCommand.TS(token);
		}
	}

	private static class TUCommandSpec extends TCommandSpec {
		private TUCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.cmduargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.U(token);
		}
	}

	private static class TWCommandSpec extends TCommandSpec {
		private TWCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.writeargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.W(token);
		}
	}

	private static class TVCommandSpec extends TCommandSpec {
		private TVCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, new TFGenericArgument("vargument"));
		}
		
		public Token getToken(Token token) {
			return new MCommand.V(token);
		}
	}

	private static class TXCommandSpec extends TCommandSpec {
		private TXCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, supply.xecuteargs);
		}
	
		public Token getToken(Token token) {
			return new MCommand.X(token);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private TGenericCommandSpec(StringPiece value, MTFSupply supply) {
			super(value, new TFGenericArgument("genericargument"));
		}
	
		public Token getToken(Token token) {
			return new MCommand.Generic(token);
		}
	}
	
	private static abstract class TCSFactory {
		public abstract TCommandSpec get(StringPiece name);
	}
	
	public void addCommands(final MTFSupply supply) {
		TCSFactory b = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TBCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("B", b);
		this.commandSpecs.put("BREAK", b); 	
		
		TCSFactory c = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("C", c);
		this.commandSpecs.put("CLOSE", c); 	
		
		TCSFactory d = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TDCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("D", d);
		this.commandSpecs.put("DO", d); 	
		
		TCSFactory e = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TECommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("E", e);
		this.commandSpecs.put("ELSE", e); 	

		TCSFactory f = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TFCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("F", f);
		this.commandSpecs.put("FOR", f); 	
		
		TCSFactory g = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TGCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("G", g);
		this.commandSpecs.put("GOTO", g); 	
		
		TCSFactory h = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new THCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("H", h);
		this.commandSpecs.put("HALT", h); 	
		this.commandSpecs.put("HANG", h); 
		
		TCSFactory i = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TICommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("I", i);
		this.commandSpecs.put("IF", i); 	
		
		TCSFactory j = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TJCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("J", j);
		this.commandSpecs.put("JOB", j); 	
		
		TCSFactory k = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TKCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("K", k);
		this.commandSpecs.put("KILL", k); 	
		
		TCSFactory l = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TLCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("L", l);
		this.commandSpecs.put("LOCK", l); 	
		
		TCSFactory m = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TMCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("M", m);
		this.commandSpecs.put("MERGE", m); 
		
		TCSFactory n = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TNCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("N", n);
		this.commandSpecs.put("NEW", n);		
		
		TCSFactory o = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TOCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("O", o);
		this.commandSpecs.put("OPEN", o); 	
		
		TCSFactory q = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TQCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("Q", q);
		this.commandSpecs.put("QUIT", q); 	
		
		TCSFactory r = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("R", r);
		this.commandSpecs.put("READ", r); 	
		
		TCSFactory s = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("S", s);
		this.commandSpecs.put("SET", s); 	
		
		TCSFactory tc = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TTCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TC", tc);
		this.commandSpecs.put("TCOMMIT", tc); 	
		
		TCSFactory tr = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TTRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TR", tr);
		this.commandSpecs.put("TRESTART", tr); 	
		
		TCSFactory tro = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TTROCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TRO", tro);
		this.commandSpecs.put("TROLLBACK", tro); 	
		
		TCSFactory ts = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TTSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TS", ts);
		this.commandSpecs.put("TSTART", ts); 	
		
		TCSFactory u = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TUCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("U", u);
		this.commandSpecs.put("USE", u);
		
		TCSFactory v = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TVCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("V", v);
		this.commandSpecs.put("VIEW", v); 	
		
		TCSFactory w = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TWCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("W", w);
		this.commandSpecs.put("WRITE", w);	
		
		TCSFactory x = new TCSFactory() {			
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TXCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("X", x);
		this.commandSpecs.put("XECUTE", x);		
	}
	
	public void addCommand(String name, final MTFSupply supply) {
		TCSFactory generic = new TCSFactory() {		
			@Override
			public TCommandSpec get(StringPiece name) {
				return new TGenericCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put(name, generic);
	}
	
	@Override
	public TokenFactory getSupplyTokenFactory() {
		return this.new TFCommandName("command.name", this.supply.ident);
	}
	
	private class TFCommandRest extends TFSequence {
		public TFCommandRest(String name, TokenFactory... factories) {
			super(name, factories);
		}
		
		@Override
		protected ValidateResult validateNull(int seqIndex, TSequence foundTokens, boolean noException) throws SyntaxErrorException {
			if (seqIndex == 3) {
				if (noException) return ValidateResult.NULL_RESULT;
				throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX);				
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
		public Token tokenize(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
			Token token = this.slave.tokenize(text, objectSupply);
			if (token == null) {
				return null;
			} else {
				StringPiece cmdName = token.toValue();
				TCSFactory tcs = TFCommand.this.commandSpecs.get(token.toValue().toString().toUpperCase());
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
		TFSequence tf = new TFCommandRest(this.getName(), this.supply.postcondition, this.supply.space, argumentFactory, this.supply.commandend);
		tf.setRequiredFlags(false, false, false, false);
		return tf;
	}

		
	@Override
	public Token tokenize(Text text, ObjectSupply objectSupply) {
		Text textCopy = text.getCopy();
		try {
			return super.tokenize(text, objectSupply);
		} catch (SyntaxErrorException e) {
			int errorIndex = text.getIndex();
			int lengthToEOL = textCopy.findEOL();
			TString t = textCopy.extractToken(lengthToEOL, objectSupply);
			text.copyFrom(textCopy);
			return new MSyntaxError(e.getCode(), t, errorIndex);
		}
	}
	
	
	@Override
	public Token getToken(Token supplyToken, Token nextToken) {
		StringPiece cmdName = supplyToken.toValue();
		TCSFactory tcs = this.commandSpecs.get(cmdName.toString().toUpperCase());
		TCommandSpec spec = tcs.get(cmdName);
		MSequence result = new MSequence(2);
		result.addToken(supplyToken);
		result.addToken(nextToken);
		return spec.getToken(result);
	}
}
