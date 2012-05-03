package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.ChoiceSupply;
import com.raygroupintl.bnf.TArray;
import com.raygroupintl.bnf.TBasic;
import com.raygroupintl.bnf.TEmpty;
import com.raygroupintl.bnf.TFBasic;
import com.raygroupintl.bnf.TFChar;
import com.raygroupintl.bnf.TFChoice;
import com.raygroupintl.bnf.TFConstChar;
import com.raygroupintl.bnf.TFConstChars;
import com.raygroupintl.bnf.TFDelimitedList;
import com.raygroupintl.bnf.TFEmptyVerified;
import com.raygroupintl.bnf.TFSeq;
import com.raygroupintl.bnf.TFSeqORO;
import com.raygroupintl.bnf.TFSeqRO;
import com.raygroupintl.bnf.TFSeqROO;
import com.raygroupintl.bnf.TFSeqRequired;
import com.raygroupintl.bnf.TSyntaxError;
import com.raygroupintl.fnds.IToken;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;

public class TFCommand extends TFSeq {
	private MVersion version;
	
	private TFCommand(MVersion version) {
		this.version = version;
	}
	
	private static abstract class TCommand extends TArray {
		public TCommand(IToken[] tokens) {
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
	
	private static class TFGenericArgument implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int fromIndex) {
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
		private ITokenFactory argumentFactory;
		
		public TCommandSpec(String value) {
			super(value);
		}

		public ITokenFactory getArgumentFactory(final MVersion version) {
			if (this.argumentFactory == null) {
				this.argumentFactory = this.buildArgumentFactory(version);
			}
			return this.argumentFactory;
		}

		protected abstract ITokenFactory buildArgumentFactory(final MVersion version);	
		
		public abstract IToken getToken(IToken[] tokens);
	}
		
	private static class TCommandB extends TCommand {
		public TCommandB(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandB(tokens);
		}
	}
	
	private static class TCommandC extends TCommand {
		public TCommandC(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
		return TFCommaDelimitedList.getInstance(new TFChoice() {			
				@Override
				protected ITokenFactory getFactory(char ch) {
					if (ch == '@') {
						return MTFSupply.getInstance(version).indirection;
					} else {
						return TFSeqRO.getInstance(MTFSupply.getInstance(version).expr, TFSeqRequired.getInstance(TFConstChar.getInstance(':'), MTFSupply.getInstance(version).deviceparams));
					}
				}
			});
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandC(tokens);
		}
	}
	
	private static class TCommandD extends TCommand {
		public TCommandD(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFDoArgument.getInstance(version));		
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandD(tokens);
		}
	}
	
	private static class TCommandE extends TCommand {
		public TCommandE(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandE(tokens);
		}
	}

	private static class TCommandF extends TCommand {
		public TCommandF(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			ITokenFactory tfExpr = MTFSupply.getInstance(version).expr;
			ITokenFactory tfFromTo = TFSeqRequired.getInstance(TFConstChar.getInstance(':'), tfExpr);
			ITokenFactory RHS = TFSeqROO.getInstance(tfExpr, tfFromTo, tfFromTo);
			ITokenFactory RHSs = TFCommaDelimitedList.getInstance(RHS);
			return TFSeqRequired.getInstance(MTFSupply.getInstance(version).lvn, TFConstChar.getInstance('='), RHSs); 
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandF(tokens);
		}
	}

	private static class TCommandG extends TCommand {
		public TCommandG(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFGotoArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandG(tokens);
		}
	}

	private static class TCommandH extends TCommand {
		public TCommandH(IToken[] tokens) {
			super(tokens);
		}		
		
		@Override
		protected String getFullName() {
			IToken argument = this.get(3);
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandH(tokens);
		}
	}

	private static class TCommandI extends TCommand {
		public TCommandI(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(MTFSupply.getInstance(version).expr); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandI(tokens);
		}
	}

	private static class TCommandJ extends TCommand {
		public TCommandJ(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFJobArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandJ(tokens);
		}
	}

	private static class TCommandK extends TCommand {
		public TCommandK(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).cmdkargs;			
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandK(tokens);
		}
	}

	private static class TCommandL extends TCommand {
		public TCommandL(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			ITokenFactory tfNRef = ChoiceSupply.get(MTFSupply.getInstance(version).lvn, "^@", MTFSupply.getInstance(version).gvn, MTFSupply.getInstance(version).indirection);		
			ITokenFactory tfNRefOrList = ChoiceSupply.get(tfNRef, '(', TFDelimitedList.getInstance(tfNRef, ',', true));
			ITokenFactory e = TFSeqORO.getInstance(TFConstChars.getInstance("+-"), tfNRefOrList,  MTFSupply.getInstance(version).timeout);
			return TFCommaDelimitedList.getInstance(e);
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandL(tokens);
		}
	}

	private static class TCommandM extends TCommand {
		public TCommandM(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFMergeArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandM(tokens);
		}
	}

	private static class TCommandN extends TCommand {
		public TCommandN(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			ITokenFactory f = new TFChoice() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					switch(ch) {
					case '(': 
						return TFCommaDelimitedList.getInstance(MTFSupply.getInstance(version).lvn);
					case '@':
						return MTFSupply.getInstance(version).indirection;
					case '$':
						return MTFSupply.getInstance(version).intrinsic;
					default:
						return TFName.getInstance();
					}
				}
			};		
			return TFDelimitedList.getInstance(f, ',');
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandN(tokens);
		}
	}

	private static class TCommandO extends TCommand {
		public TCommandO(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFOpenArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandO(tokens);
		}
	}

	private static class TCommandQ extends TCommand {
		public TCommandQ(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return MTFSupply.getInstance(version).expr; 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandQ(tokens);
		}
	}

	private static class TCommandR extends TCommand {
		public TCommandR(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFReadArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandR(tokens);
		}
	}

	private static class TCommandS extends TCommand {
		public TCommandS(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFSetArgument.getInstance(version)); 	
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandS(tokens);
		}
	}

	private static class TCommandTC extends TCommand {
		public TCommandTC(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandTC(tokens);
		}
	}

	private static class TCommandTR extends TCommand {
		public TCommandTR(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandTR(tokens);
		}
	}

	private static class TCommandTRO extends TCommand {
		public TCommandTRO(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandTRO(tokens);
		}
	}

	private static class TCommandTS extends TCommand {
		public TCommandTS(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TF_EMPTY;
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandTS(tokens);
		}
	}

	private static class TCommandU extends TCommand {
		public TCommandU(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return TFCommaDelimitedList.getInstance(TFUseArgument.getInstance(version));			
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandU(tokens);
		}
	}

	private static class TCommandW extends TCommand {
		public TCommandW(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			ITokenFactory f = new TFChoice() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					switch(ch) {
						case '!':
						case '#':
						case '?':
							return TFFormat.getInstance(version);
						case '/':
							return TFSeqRequired.getInstance(TFChar.SLASH, TFName.getInstance(), MTFSupply.getInstance(version).actuallist);
						case '*':
							return TFSeqRequired.getInstance(TFConstChar.getInstance('*'), MTFSupply.getInstance(version).expr);
						case '@':
							return MTFSupply.getInstance(version).indirection;
						default:
							return MTFSupply.getInstance(version).expr;
					}
				}
			};	
			return TFDelimitedList.getInstance(f, ',');
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandW(tokens);
		}
	}

	private static class TCommandV extends TCommand {
		public TCommandV(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return new TFGenericArgument();
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandV(tokens);
		}
	}

	private static class TCommandX extends TCommand {
		public TCommandX(IToken[] tokens) {
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
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			ITokenFactory tf = ChoiceSupply.get(MTFSupply.getInstance(version).expr, '@', MTFSupply.getInstance(version).indirection);
			ITokenFactory pc = MTFSupply.getInstance(version).postcondition;
			return TFDelimitedList.getInstance(TFSeqRO.getInstance(tf, pc), ',');
		}
		
		public IToken getToken(IToken[] tokens) {
			return new TCommandX(tokens);
		}
	}

	private static class TGenericCommandSpec extends TCommandSpec {
		private TGenericCommandSpec(String value) {
			super(value);
		}
	
		@Override
		protected ITokenFactory buildArgumentFactory(final MVersion version) {
			return new TFGenericArgument();
		}
		
		public IToken getToken(IToken[] tokens) {
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
		public IToken tokenize(String line, int fromIndex) {
			IToken result = super.tokenize(line, fromIndex);
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
		
	private static class TFSCommand implements ITokenFactorySupply {
		private MVersion version;
		
		private TFSCommand(MVersion version) {
			this.version = version;
		}
			
		public ITokenFactory get(int seqIndex, IToken[] previousTokens) {
			switch (seqIndex) {
				case 0:
					return TFCommandName.getInstance();
				case 1:
					return TFSeqRequired.getInstance(TFConstChar.getInstance(':'), MTFSupply.getInstance(version).expr);
				case 2:
					return TFConstChar.getInstance(' ');
				case 3: {
					TCommandSpec cmd = (TCommandSpec) previousTokens[0];
					ITokenFactory f = cmd.getArgumentFactory(this.version);
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
	protected ITokenFactorySupply getFactorySupply() {
		return new TFSCommand(this.version);
	}

	@Override
	protected int validateNull(int seqIndex, IToken[] foundTokens) {
		if (seqIndex == 0) {
			return RETURN_NULL;
		}
		return CONTINUE;				
	}

	@Override
	protected int validateEnd(int seqIndex, IToken[] foundTokens) {
		return 0;
	}

	@Override
	public IToken getToken(IToken[] tokens) {
		TCommandSpec spec = (TCommandSpec) tokens[0];
		return spec.getToken(tokens);
	}
			
	public static TFCommand getInstance(MVersion version) {
		return new TFCommand(version);
	}
}
