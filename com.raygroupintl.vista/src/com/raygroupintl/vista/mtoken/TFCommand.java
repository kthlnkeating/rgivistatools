package com.raygroupintl.vista.mtoken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TBasic;
import com.raygroupintl.vista.token.TEmpty;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFBasic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFConstChars;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFParallelCharBased;
import com.raygroupintl.vista.token.TFSerialBase;
import com.raygroupintl.vista.token.TFSerialORO;
import com.raygroupintl.vista.token.TFSerialRO;
import com.raygroupintl.vista.token.TFSerialROO;
import com.raygroupintl.vista.token.TSyntaxError;

public class TFCommand extends TFSerialBase {
	private MVersion version;
	
	private TFCommand(MVersion version) {
		this.version = version;
	}
		
	private static MNameWithMnemonic.Map COMMAND_NAMES = new MNameWithMnemonic.Map();
	static {
		COMMAND_NAMES.update("B", "BREAK"); 	
		COMMAND_NAMES.update("H", "HALT"); 	
		COMMAND_NAMES.update("H", "HANG"); 	
		COMMAND_NAMES.update("V", "VIEW"); 	
		COMMAND_NAMES.update("E", "ELSE"); 	
		COMMAND_NAMES.update("TC", "TCOMMIT"); 	
		COMMAND_NAMES.update("TR", "TRESTART"); 	
		COMMAND_NAMES.update("TRO", "TROLLBACK"); 	
		COMMAND_NAMES.update("TS", "TSTART"); 	
		COMMAND_NAMES.update("C", "CLOSE"); 	
		COMMAND_NAMES.update("D", "DO"); 	
		COMMAND_NAMES.update("E", "XECUTE"); 	
		COMMAND_NAMES.update("F", "FOR"); 	
		COMMAND_NAMES.update("G", "GOTO"); 	
		COMMAND_NAMES.update("I", "IF"); 	
		COMMAND_NAMES.update("J", "JOB"); 	
		COMMAND_NAMES.update("K", "KILL"); 	
		COMMAND_NAMES.update("L", "LOCK"); 	
		COMMAND_NAMES.update("M", "MERGE"); 	
		COMMAND_NAMES.update("N", "NEW");		
		COMMAND_NAMES.update("O", "OPEN"); 	
		COMMAND_NAMES.update("Q", "QUIT"); 	
		COMMAND_NAMES.update("R", "READ"); 	
		COMMAND_NAMES.update("S", "SET"); 	
		COMMAND_NAMES.update("U", "USE");
		COMMAND_NAMES.update("W", "WRITE");		
		COMMAND_NAMES.update("X", "XECUTE");		
	}
	
	public static ITokenFactory getTFPostCondition(IToken[] previousTokens, MVersion version) {
		ITokenFactory tfColon = TFConstChar.getInstance(':');
		ITokenFactory tfExpr = TFExpr.getInstance(version);
		return TFAllRequired.getInstance(tfColon, tfExpr);
	}

	private static ITokenFactory getXArgumentFactory(MVersion version) {
		ITokenFactory tf = TFParallelCharBased.getInstance(TFExpr.getInstance(version), '@', TFIndirection.getInstance(version));
		ITokenFactory pc = getTFPostCondition(null, version);
		return TFDelimitedList.getInstance(TFSerialRO.getInstance(tf, pc), ',');
	}
	
	private static ITokenFactory getFArgumentFactory(MVersion version) {
		TFExpr tfExpr = TFExpr.getInstance(version);
		TFAllRequired tfFromTo = TFAllRequired.getInstance(TFConstChar.getInstance(':'), tfExpr);
		ITokenFactory RHS = TFSerialROO.getInstance(tfExpr, tfFromTo, tfFromTo);
		ITokenFactory RHSs = TFCommaDelimitedList.getInstance(RHS);
		return TFAllRequired.getInstance(TFLvn.getInstance(version), TFConstChar.getInstance('='), RHSs); 
	}

	private static ITokenFactory getLArgumentFactory(MVersion version) {
		ITokenFactory tfNRef = TFParallelCharBased.getInstance(TFLvn.getInstance(version), '^', TFGvn.getInstance(version), '@', TFIndirection.getInstance(version));		
		ITokenFactory tfNRefOrList = TFParallelCharBased.getInstance(tfNRef, '(', TFDelimitedList.getInstance(tfNRef, ',', true));
		ITokenFactory e = TFSerialORO.getInstance(TFConstChars.getInstance("+-"), tfNRefOrList, TFTimeout.getInstance(version));
		return TFCommaDelimitedList.getInstance(e);
	}


	
	public static void addCommand(String name) {
		COMMAND_NAMES.update(name, name);
	}
	
	private static class TCommandName extends TKeyword {
		private TCommandName(String value) {
			super(value);
		}
	
		@Override
		public List<MError> getErrors() {
			return null;
		}
		
		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String value = this.getStringValue().toUpperCase();
			MNameWithMnemonic name = COMMAND_NAMES.get(value);
			return name;
		}
	}
	
	private static class TFGeneric implements ITokenFactory {
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
		
	private static class TFCommandName extends TFIdent {
		@Override
		public IToken tokenize(String line, int fromIndex) {
			IToken result = super.tokenize(line, fromIndex);
			String cmdName = result.getStringValue().toUpperCase();
			if (COMMAND_NAMES.containsKey(cmdName)) {
				return new TCommandName(result.getStringValue());
			} else {
				return new TSyntaxError(MError.ERR_UNDEFINED_COMMAND , line, fromIndex);
			}			
		}
				
		public static TFCommandName getInstance() {
			return new TFCommandName();
		}
	}
		
	private static class TFSCommand implements ITokenFactorySupply {
		private static Map<String, ITokenFactory> ARGUMENT_FACTORIES;
		private static void initialize(final MVersion version) {
			ARGUMENT_FACTORIES = new HashMap<String, ITokenFactory>();
			TFEmpty empty = TFEmpty.getInstance();
			ARGUMENT_FACTORIES.put("E", empty); 	
			ARGUMENT_FACTORIES.put("TC", empty); 	
			ARGUMENT_FACTORIES.put("TR", empty); 	
			ARGUMENT_FACTORIES.put("TRO", empty); 	
			ARGUMENT_FACTORIES.put("TS", empty); 	
			
			ITokenFactory c = TFCommaDelimitedList.getInstance(new TFParallelCharBased() {			
				@Override
				protected ITokenFactory getFactory(char ch) {
					if (ch == '@') {
						return TFIndirection.getInstance(version);
					} else {
						return TFSerialRO.getInstance(TFExpr.getInstance(version), TFAllRequired.getInstance(TFConstChar.getInstance(':'), new TFDeviceParams(version)));
					}
				}
			});
			ARGUMENT_FACTORIES.put("C", c); 	
			
			
			ARGUMENT_FACTORIES.put("D", TFCommaDelimitedList.getInstance(TFDoArgument.getInstance(version)));		
			ARGUMENT_FACTORIES.put("X", getXArgumentFactory(version)); 	
			ARGUMENT_FACTORIES.put("F", getFArgumentFactory(version)); 	
			ARGUMENT_FACTORIES.put("G", TFCommaDelimitedList.getInstance(TFGotoArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("I", TFCommaDelimitedList.getInstance(TFExpr.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("J", TFCommaDelimitedList.getInstance(TFJobArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("K", TFCommaDelimitedList.getInstance(TFKillArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("L", getLArgumentFactory(version)); 	
			ARGUMENT_FACTORIES.put("M", TFCommaDelimitedList.getInstance(TFMergeArgument.getInstance(version))); 	
			
			ITokenFactory n = new TFParallelCharBased() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					switch(ch) {
					case '(': 
						return TFCommaDelimitedList.getInstance(TFLvn.getInstance(version));
					case '@':
						return TFIndirection.getInstance(version);
					case '$':
						return TFIntrinsic.getInstance(version);
					default:
						return TFName.getInstance();
					}
				}
			};		
			ARGUMENT_FACTORIES.put("N", TFCommaDelimitedList.getInstance(n));		
			
			ARGUMENT_FACTORIES.put("O", TFCommaDelimitedList.getInstance(TFOpenArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("Q", TFParallelCharBased.getInstance(TFExpr.getInstance(version), '@', TFIndirection.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("R", TFCommaDelimitedList.getInstance(TFReadArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("S", TFCommaDelimitedList.getInstance(TFSetArgument.getInstance(version))); 	
			ARGUMENT_FACTORIES.put("U", TFCommaDelimitedList.getInstance(TFUseArgument.getInstance(version)));
			
			ITokenFactory w = new TFParallelCharBased() {
				@Override
				protected ITokenFactory getFactory(char ch) {
					switch(ch) {
						case '!':
						case '#':
						case '?':
						case '/':
							return TFFormat.getInstance(version);
						case '*':
							return TFAllRequired.getInstance(TFConstChar.getInstance('*'), TFExpr.getInstance(version));
						case '@':
							return TFIndirection.getInstance(version);
						default:
							return TFExpr.getInstance(version);
					}
				}
			};		
			ARGUMENT_FACTORIES.put("W", TFCommaDelimitedList.getInstance(w));		
		}
		
		private MVersion version;
		
		private TFSCommand(MVersion version) {
			this.version = version;
		}
			
		public ITokenFactory get(IToken[] previousTokens) {
			int n = previousTokens.length;
			switch (n) {
				case 0:
					return TFCommandName.getInstance();
				case 1:
					return TFAllRequired.getInstance(TFConstChar.getInstance(':'), TFExpr.getInstance(this.version));
				case 2:
					return TFConstChar.getInstance(' ');
				case 3: {
					TCommandName cmd = (TCommandName) previousTokens[0];
					String key = cmd.getNameWithMnemonic().getMnemonic();
					if (ARGUMENT_FACTORIES == null) {
						initialize(this.version);
					}
					ITokenFactory f = ARGUMENT_FACTORIES.get(key);
					if (f == null) {
						return new TFGeneric();
					} else {
						return f;
					}
				}					
				case 4:
					return TFBasic.getInstance(' ');
				default:
					assert(n == 5);
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
	protected int getCodeNextIsNull(IToken[] foundTokens) {
		int n = foundTokens.length;
		if (n == 0) {
			return RETURN_NULL;
		}
		if (n == 2) {
			return this.getErrorCode();
		}
		return CONTINUE;				
	}

	@Override
	protected int getCodeStringEnds(IToken[] foundTokens) {
		return 0;
	}

	public static TFCommand getInstance(MVersion version) {
		return new TFCommand(version);
	}
}
