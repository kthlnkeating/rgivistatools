package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.struct.MError;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFEmptyVerified;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.TokenFactorySupply;
import com.raygroupintl.parsergen.ObjectSupply;

public class TFCommand extends TokenFactorySupply<MToken> {
	private Map<String, TCSFactory> commandSpecs = new HashMap<String, TCSFactory>();
	private MTFSupply supply;
	
	public TFCommand(String name, MTFSupply supply) {
		super(name);
		this.supply = supply;
	}
	
	private static class TFGenericArgument extends TokenFactory<MToken> {
		public TFGenericArgument(String name) {
			super(name);
		}
		
		@Override
		public MToken tokenizeOnly(Text text, ObjectSupply<MToken> objectSupply) {
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
				TextPiece p = text.extractPiece(index);
				return objectSupply.newString(p);
			} else {
				return new MEmpty();
			}
		}
	}

	private static final TFEmptyVerified<MToken> TF_EMPTY = new TFEmptyVerified<MToken>("commandempty", ' ');
	
	private static abstract class TCommandSpec extends MString {
		private static final long serialVersionUID = 1L;
		
		private TokenFactory<MToken> argumentFactory;
		
		public TCommandSpec(TextPiece value, TokenFactory<MToken> argumentFactory) {
			super(value);
			this.argumentFactory = argumentFactory;
		}

		public TokenFactory<MToken> getArgumentFactory() {
			return this.argumentFactory;
		}

		public abstract MToken getToken(MToken cmdToken, MToken cmdDependentTokens);
	}
		
	private static class TBCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TBCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.B(cmdName, cmdDependent);
		}
	}
	
	private static class TCCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TCCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.closearg);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new OpenCloseUseCmdTokens.MCloseCmd(cmdName, cmdDependent);
		}
	}
	
	private static class TDCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TDCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.doarguments);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.D(cmdName, cmdDependent);
		}
	}
	
	private static class TECommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TECommandSpec(TextPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.E(cmdName, cmdDependent);
		}
	}

	private static class TFCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TFCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.forarg);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new MForCmd(cmdName, cmdDependent);
		}
	}

	private static class TGCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TGCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.gotoarguments);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.G(cmdName, cmdDependent);
		}
	}

	private static class THCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private THCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.H(cmdName, cmdDependent);
		}
	}

	private static class TICommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TICommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.exprlist);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.I(cmdName, cmdDependent);
		}
	}

	private static class TJCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TJCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.cmdjargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.J(cmdName, cmdDependent);
		}
	}

	private static class TKCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TKCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.killargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new KillCmdTokens.MKillCmd(cmdName, cmdDependent);
		}
	}

	private static class TLCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TLCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.lockargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.L(cmdName, cmdDependent);
		}
	}

	private static class TMCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TMCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.mergeargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new MergeCmdTokens.MMergeCmd(cmdName, cmdDependent);
		}
	}

	private static class TNCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TNCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.newargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new NewCmdTokens.MNewCmd(cmdName, cmdDependent);
		}
	}

	private static class TOCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TOCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.cmdoargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new OpenCloseUseCmdTokens.MOpenCmd(cmdName, cmdDependent);
		}
	}

	private static class TQCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TQCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.Q(cmdName, cmdDependent);
		}
	}

	private static class TRCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TRCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.cmdrargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.R(cmdName, cmdDependent);
		}
	}

	private static class TSCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TSCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.setargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new SetCmdTokens.MSetCmd(cmdName, cmdDependent);
		}
	}

	private static class TTCCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TTCCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.TC(cmdName, cmdDependent);
		}
	}

	private static class TTRCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TTRCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.TR(cmdName, cmdDependent);
		}
	}

	private static class TTROCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TTROCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.TRO(cmdName, cmdDependent);
		}
	}

	private static class TTSCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TTSCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.TS(cmdName, cmdDependent);
		}
	}

	private static class TUCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TUCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.cmduargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new OpenCloseUseCmdTokens.MUseCmd(cmdName, cmdDependent);
		}
	}

	private static class TWCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TWCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.writeargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.W(cmdName, cmdDependent);
		}
	}

	private static class TVCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TVCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, new TFGenericArgument("vargument"));
		}
		
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.V(cmdName, cmdDependent);
		}
	}

	private static class TXCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TXCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, supply.xecuteargs);
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.X(cmdName, cmdDependent);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private static final long serialVersionUID = 1L;
		
		private TGenericCommandSpec(TextPiece value, MTFSupply supply) {
			super(value, new TFGenericArgument("genericargument"));
		}
	
		public MToken getToken(MToken cmdName, MToken cmdDependent) {
			return new CmdTokens.Generic(cmdName, cmdDependent);
		}
	}
	
	private static abstract class TCSFactory {
		public abstract TCommandSpec get(TextPiece name);
	}
	
	public void addCommands(final MTFSupply supply) {
		TCSFactory b = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TBCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("B", b);
		this.commandSpecs.put("BREAK", b); 	
		
		TCSFactory c = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("C", c);
		this.commandSpecs.put("CLOSE", c); 	
		
		TCSFactory d = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TDCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("D", d);
		this.commandSpecs.put("DO", d); 	
		
		TCSFactory e = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TECommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("E", e);
		this.commandSpecs.put("ELSE", e); 	

		TCSFactory f = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TFCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("F", f);
		this.commandSpecs.put("FOR", f); 	
		
		TCSFactory g = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TGCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("G", g);
		this.commandSpecs.put("GOTO", g); 	
		
		TCSFactory h = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new THCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("H", h);
		this.commandSpecs.put("HALT", h); 	
		this.commandSpecs.put("HANG", h); 
		
		TCSFactory i = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TICommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("I", i);
		this.commandSpecs.put("IF", i); 	
		
		TCSFactory j = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TJCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("J", j);
		this.commandSpecs.put("JOB", j); 	
		
		TCSFactory k = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TKCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("K", k);
		this.commandSpecs.put("KILL", k); 	
		
		TCSFactory l = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TLCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("L", l);
		this.commandSpecs.put("LOCK", l); 	
		
		TCSFactory m = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TMCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("M", m);
		this.commandSpecs.put("MERGE", m); 
		
		TCSFactory n = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TNCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("N", n);
		this.commandSpecs.put("NEW", n);		
		
		TCSFactory o = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TOCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("O", o);
		this.commandSpecs.put("OPEN", o); 	
		
		TCSFactory q = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TQCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("Q", q);
		this.commandSpecs.put("QUIT", q); 	
		
		TCSFactory r = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("R", r);
		this.commandSpecs.put("READ", r); 	
		
		TCSFactory s = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("S", s);
		this.commandSpecs.put("SET", s); 	
		
		TCSFactory tc = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TTCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TC", tc);
		this.commandSpecs.put("TCOMMIT", tc); 	
		
		TCSFactory tr = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TTRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TR", tr);
		this.commandSpecs.put("TRESTART", tr); 	
		
		TCSFactory tro = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TTROCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TRO", tro);
		this.commandSpecs.put("TROLLBACK", tro); 	
		
		TCSFactory ts = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TTSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TS", ts);
		this.commandSpecs.put("TSTART", ts); 	
		
		TCSFactory u = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TUCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("U", u);
		this.commandSpecs.put("USE", u);
		
		TCSFactory v = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TVCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("V", v);
		this.commandSpecs.put("VIEW", v); 	
		
		TCSFactory w = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TWCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("W", w);
		this.commandSpecs.put("WRITE", w);	
		
		TCSFactory x = new TCSFactory() {			
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TXCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("X", x);
		this.commandSpecs.put("XECUTE", x);		
	}
	
	public void addCommand(String name, final MTFSupply supply) {
		TCSFactory generic = new TCSFactory() {		
			@Override
			public TCommandSpec get(TextPiece name) {
				return new TGenericCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put(name, generic);
	}
	
	@Override
	public TokenFactory<MToken> getSupplyTokenFactory() {
		return this.new TFCommandName("command.name", this.supply.ident);
	}
	
	private class TFCommandRest extends TFSequence<MToken> {
		public TFCommandRest(String name) {
			super(name, 4);
		}
		
		@Override
		protected ValidateResult validateNull(int seqIndex, SequenceOfTokens<MToken> foundTokens, boolean noException) throws SyntaxErrorException {
			if (seqIndex == 3) {
				if (noException) return ValidateResult.NULL_RESULT;
				throw new SyntaxErrorException(MError.ERR_GENERAL_SYNTAX);				
			} else {
				return ValidateResult.CONTINUE;
			}
		}
	}

	private class TFCommandName extends TokenFactory<MToken> {
		private TokenFactory<MToken> slave;
		
		public TFCommandName(String name, TokenFactory<MToken> slave) {
			super(name);
			this.slave = slave;
		}
		
		@Override
		public MToken tokenizeOnly(Text text, ObjectSupply<MToken> objectSupply) throws SyntaxErrorException {
			Token token = this.slave.tokenize(text, objectSupply);
			if (token == null) {
				return null;
			} else {
				TextPiece cmdName = token.toValue();
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
	public TokenFactory<MToken> getNextTokenFactory(MToken token) throws SyntaxErrorException {
		TCommandSpec spec = (TCommandSpec) token;
		TokenFactory<MToken> argumentFactory = spec.getArgumentFactory();
		TFSequence<MToken> tf = new TFCommandRest(this.getName());
		tf.add(this.supply.postcondition, false);
		tf.add(this.supply.space, false);
		tf.add(argumentFactory, false);
		tf.add(this.supply.commandend, false);
		return tf;
	}

		
	@Override
	public MToken tokenizeOnly(Text text, ObjectSupply<MToken> objectSupply) {
		Text textCopy = text.getCopy();
		try {
			return super.tokenizeOnly(text, objectSupply);
		} catch (SyntaxErrorException e) {
			int errorIndex = text.getIndex();
			int lengthToEOL = textCopy.findEOL();
			TextPiece t = textCopy.extractPiece(lengthToEOL);
			text.copyFrom(textCopy);
			return new MSyntaxError(e.getCode(), t, errorIndex);
		}
	}
	
	
	@Override
	public MToken getToken(MToken supplyToken, MToken nextToken) {
		TCommandSpec spec = (TCommandSpec) supplyToken;
		return spec.getToken(supplyToken, nextToken);
	}
}
