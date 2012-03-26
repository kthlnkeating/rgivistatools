package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.fnds.ITokenFactorySupply;
import com.raygroupintl.vista.mtoken.command.TCommandClose;
import com.raygroupintl.vista.mtoken.command.TCommandDo;
import com.raygroupintl.vista.mtoken.command.TCommandExecute;
import com.raygroupintl.vista.mtoken.command.TCommandFor;
import com.raygroupintl.vista.mtoken.command.TCommandGoto;
import com.raygroupintl.vista.mtoken.command.TCommandIf;
import com.raygroupintl.vista.mtoken.command.TCommandJob;
import com.raygroupintl.vista.mtoken.command.TCommandKill;
import com.raygroupintl.vista.mtoken.command.TCommandLock;
import com.raygroupintl.vista.mtoken.command.TCommandMerge;
import com.raygroupintl.vista.mtoken.command.TCommandNew;
import com.raygroupintl.vista.mtoken.command.TCommandOpen;
import com.raygroupintl.vista.mtoken.command.TCommandQuit;
import com.raygroupintl.vista.mtoken.command.TCommandRead;
import com.raygroupintl.vista.mtoken.command.TCommandSet;
import com.raygroupintl.vista.mtoken.command.TCommandUse;
import com.raygroupintl.vista.mtoken.command.TCommandWrite;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TFAllRequired;
import com.raygroupintl.vista.token.TFAnyBut;
import com.raygroupintl.vista.token.TFBasic;
import com.raygroupintl.vista.token.TFConstChar;
import com.raygroupintl.vista.token.TFEmpty;
import com.raygroupintl.vista.token.TFSerialBase;

public class TFCommand extends TFSerialBase {
	private static abstract class CommandFactory {
		public abstract TCommandName getInstance(String identifier);
	}

	protected static abstract class TCommandAnyArgument extends TCommandName {
		public TCommandAnyArgument(String identifier) {
			super(identifier);
		}
		
		@Override
		public ITokenFactory getArgumentFactory() {
			return TFAnyBut.getInstance();
		}		
	}
	
	private static class TCommandBreak extends TCommandAnyArgument {
		public TCommandBreak(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("B", "BREAK");
		}		
	}
	
	private static class TCommandHalt extends TCommandAnyArgument {
		public TCommandHalt(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("H", "HALT");
		}		
	}

	private static class TCommandHang extends TCommandAnyArgument {
		public TCommandHang(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("H", "HANG");
		}		
	}

	private static class TCommandView extends TCommandAnyArgument {
		public TCommandView(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("V", "VIEW");
		}		
	}
	
	private static class TCommandGeneric extends TCommandAnyArgument {
		public TCommandGeneric(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			String id = this.getIdentier();
			return new MNameWithMnemonic(id, id);
		}	
		
		public List<MError> auxGetErrors() {
			String id = this.getIdentier();
			char ch = id.charAt(0);
			if (ch == 'Z' || ch == 'z') {
				return null;
			} else if (Character.isLetterOrDigit(ch)) {
				return Arrays.asList(new MError[]{new MError(MError.ERR_UNDEFINED_COMMAND)});
			} else {
				return Arrays.asList(new MError[]{new MError(MError.ERR_GENERAL_SYNTAX)});				
			}
		}		
		
		@Override
		public List<MError> getErrors() {
			List<MError> errors = this.auxGetErrors();
			List<MError> parentErrors = super.getErrors();
			if (errors == null) {
				return parentErrors;
			} else {
				if (parentErrors != null) {
					errors.addAll(parentErrors);
				}
				return errors;
			}
		}		
	}

	protected static abstract class TCommandNoArgument extends TCommandName {
		public TCommandNoArgument(String identifier) {
			super(identifier);
		}
		
		@Override
		public ITokenFactory getArgumentFactory() {
			return TFEmpty.getInstance();
		}		
	}
	
	private static class TCommandElse extends TCommandNoArgument {
		public TCommandElse(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("E", "ELSE");
		}		
	}

	private static class TCommandTCommit extends TCommandNoArgument {
		public TCommandTCommit(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TC", "TCOMMIT");
		}		
	}
	
	private static class TCommandTRestart extends TCommandNoArgument {
		public TCommandTRestart(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TR", "TRESTART");
		}		
	}
	
	private static class TCommandTRollback extends TCommandNoArgument {
		public TCommandTRollback(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TRO", "TROLLBACK");
		}		
	}
	
	private static class TCommandTStart extends TCommandNoArgument {
		public TCommandTStart(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TS", "TSTART");
		}		
	}
	
	private static final Map<String, CommandFactory> COMMANDS = new HashMap<String, CommandFactory>();
	static {
		CommandFactory b = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandBreak(identifier);
			}
		};
		COMMANDS.put("B", b);
		COMMANDS.put("BREAK", b);
		CommandFactory c = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandClose(identifier);
			}
		};		
		COMMANDS.put("C", c);
		COMMANDS.put("CLOSE", c);
		CommandFactory d = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandDo(identifier);
			}
		};				
		COMMANDS.put("D", d);
		COMMANDS.put("DO", d);
		CommandFactory e = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandElse(identifier);
			}
		};						
		COMMANDS.put("E", e);
		COMMANDS.put("ELSE", e);
		CommandFactory f = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandFor(identifier);
			}
		};						
		COMMANDS.put("F", f);
		COMMANDS.put("FOR", f);
		CommandFactory g = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandGoto(identifier);
			}
		};						
		COMMANDS.put("G", g);
		COMMANDS.put("GOTO", g);
		CommandFactory h = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandHalt(identifier);
			}
		};								
		COMMANDS.put("H", h);
		COMMANDS.put("HALT", h);
		CommandFactory hh = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandHang(identifier);
			}
		};										
		COMMANDS.put("H", hh);
		COMMANDS.put("HANG", hh);
		CommandFactory i = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandIf(identifier);
			}
		};												
		COMMANDS.put("I", i);
		COMMANDS.put("IF", i);
		CommandFactory j = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandJob(identifier);
			}
		};												
		COMMANDS.put("J", j);
		COMMANDS.put("JOB", j);
		CommandFactory k = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandKill(identifier);
			}
		};												
		COMMANDS.put("K", k);
		COMMANDS.put("KILL", k);
		CommandFactory l = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandLock(identifier);
			}
		};												
		COMMANDS.put("L", l);
		COMMANDS.put("LOCK", l);
		CommandFactory m = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandMerge(identifier);
			}
		};												
		COMMANDS.put("M", m);
		COMMANDS.put("MERGE", m);
		CommandFactory n = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandNew(identifier);
			}
		};												
		COMMANDS.put("N", n);
		COMMANDS.put("NEW", n);
		CommandFactory o = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandOpen(identifier);
			}
		};												
		COMMANDS.put("O", o);
		COMMANDS.put("OPEN", o);
		CommandFactory q = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandQuit(identifier);
			}
		};												
		COMMANDS.put("Q", q);
		COMMANDS.put("QUIT", q);
		CommandFactory r = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandRead(identifier);
			}
		};												
		COMMANDS.put("R", r);
		COMMANDS.put("READ", r);
		CommandFactory s = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandSet(identifier);
			}
		};												
		COMMANDS.put("S", s);
		COMMANDS.put("SET", s);
		CommandFactory tc = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandTCommit(identifier);
			}
		};												
		COMMANDS.put("TC", tc);
		COMMANDS.put("TCOMMIT", tc);
		CommandFactory tr = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandTRestart(identifier);
			}
		};												
		COMMANDS.put("TR", tr);
		COMMANDS.put("TRESTART", tr);
		CommandFactory tro = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandTRollback(identifier);
			}
		};												
		COMMANDS.put("TRO", tro);
		COMMANDS.put("TROLLBACK", tro);
		CommandFactory ts = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandTStart(identifier);
			}
		};												
		COMMANDS.put("TS", ts);
		COMMANDS.put("TSTART", ts);
		CommandFactory u = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandUse(identifier);
			}
		};												
		COMMANDS.put("U", u);
		COMMANDS.put("USE", u);
		CommandFactory v = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandView(identifier);
			}
		};												
		COMMANDS.put("V", v);
		COMMANDS.put("VIEW", v);
		CommandFactory w = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandWrite(identifier);
			}
		};												
		COMMANDS.put("W", w);
		COMMANDS.put("WRITE", w);
		CommandFactory x = new CommandFactory() {			
			@Override
			public TCommandName getInstance(String identifier) {
				return new TCommandExecute(identifier);
			}
		};												
		COMMANDS.put("X", x);
		COMMANDS.put("XECUTE", x);
	}
	
	private static TCommandName getCommand(String identifier) {
		CommandFactory f = COMMANDS.get(identifier.toUpperCase());
		if (f != null) {
			TCommandName command = f.getInstance(identifier);
			return command;
		} else {
			TCommandName command = new TCommandGeneric(identifier);
			return command;
		}
	}
	
	private static class TFIdentCommand extends TFIdent {
		@Override
		protected TCommandName getToken(String value) {
			return getCommand(value);
		}				
		
		public static TFIdentCommand getInstance() {
			return new TFIdentCommand();
		}
	}
		
	private static class TFSCommand implements ITokenFactorySupply {
		public ITokenFactory get(IToken[] previousTokens) {
			int n = previousTokens.length;
			switch (n) {
				case 0:
					return TFIdentCommand.getInstance();
				case 1:
					return TFAllRequired.getInstance(TFConstChar.getInstance(':'), TFExpr.getInstance());
				case 2:
					return TFConstChar.getInstance(' ');
				case 3: {
					TCommandName cmd = (TCommandName) previousTokens[0];
					return cmd.getArgumentFactory();
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
		return new TFSCommand();
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
}
