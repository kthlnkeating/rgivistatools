package com.raygroupintl.vista.mtoken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.command.TCommandDo;
import com.raygroupintl.vista.mtoken.command.TCommandGoto;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MNameWithMnemonic;
import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TSyntaxError;

public class Line extends TList {
	private static abstract class CommandFactory {
		public abstract TCommand getInstance(String identifier);
	}

	private static class BreakCommand extends TCommand {
		public BreakCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("B", "BREAK");
		}		
	}
	
	private static class CloseCommand extends TCommand {
		public CloseCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("C", "CLOSE");
		}		
	}

	private static class ElseCommand extends TCommand {
		public ElseCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("E", "ELSE");
		}		
	}

	private static class ForCommand extends TCommand {
		public ForCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("F", "FOR");
		}		
	}

	private static class HaltCommand extends TCommand {
		public HaltCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("H", "HALT");
		}		
	}

	private static class HangCommand extends TCommand {
		public HangCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("H", "HANG");
		}		
	}

	private static class IfCommand extends TCommand {
		public IfCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("I", "IF");
		}		
	}
	
	private static class JobCommand extends TCommand {
		public JobCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("J", "JOB");
		}		
	}
	
	private static class KillCommand extends TCommand {
		public KillCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("K", "KILL");
		}		
	}
	
	private static class LockCommand extends TCommand {
		public LockCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("L", "LOCK");
		}		
	}
	
	private static class MergeCommand extends TCommand {
		public MergeCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("M", "MERGE");
		}		
	}
	
	private static class NewCommand extends TCommand {
		public NewCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("N", "NEW");
		}		
	}
	
	private static class OpenCommand extends TCommand {
		public OpenCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("O", "OPEN");
		}		
	}
	
	private static class QuitCommand extends TCommand {
		public QuitCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("Q", "QUIT");
		}		
	}
	
	private static class ReadCommand extends TCommand {
		public ReadCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("R", "READ");
		}		
	}
	
	private static class SetCommand extends TCommand {
		public SetCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("S", "SET");
		}		
	}
	
	private static class TCommitCommand extends TCommand {
		public TCommitCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TC", "TCOMMIT");
		}		
	}
	
	private static class TRestartCommand extends TCommand {
		public TRestartCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TR", "TRESTART");
		}		
	}
	
	private static class TRollbackCommand extends TCommand {
		public TRollbackCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TRO", "TROLLBACK");
		}		
	}
	
	private static class TStartCommand extends TCommand {
		public TStartCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("TS", "TSTART");
		}		
	}
	
	private static class UseCommand extends TCommand {
		public UseCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("U", "USE");
		}		
	}
	
	
	private static class ViewCommand extends TCommand {
		public ViewCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("V", "VIEW");
		}		
	}
	
	
	private static class WriteCommand extends TCommand {
		public WriteCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("W", "WRITE");
		}		
	}
	
	
	private static class XecuteCommand extends TCommand {
		public XecuteCommand(String identifier) {
			super(identifier);
		}

		@Override
		protected MNameWithMnemonic getNameWithMnemonic() {
			return new MNameWithMnemonic("X", "XECUTE");
		}		
	}
	
	private static class GenericCommand extends TCommand {
		public GenericCommand(String identifier) {
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

	private static final Map<String, CommandFactory> COMMANDS = new HashMap<String, CommandFactory>();
	static {
		CommandFactory b = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new BreakCommand(identifier);
			}
		};
		COMMANDS.put("B", b);
		COMMANDS.put("BREAK", b);
		CommandFactory c = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new CloseCommand(identifier);
			}
		};		
		COMMANDS.put("C", c);
		COMMANDS.put("CLOSE", c);
		CommandFactory d = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TCommandDo(identifier);
			}
		};				
		COMMANDS.put("D", d);
		COMMANDS.put("DO", d);
		CommandFactory e = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new ElseCommand(identifier);
			}
		};						
		COMMANDS.put("E", e);
		COMMANDS.put("ELSE", e);
		CommandFactory f = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new ForCommand(identifier);
			}
		};						
		COMMANDS.put("F", f);
		COMMANDS.put("FOR", f);
		CommandFactory g = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TCommandGoto(identifier);
			}
		};						
		COMMANDS.put("G", g);
		COMMANDS.put("GOTO", g);
		CommandFactory h = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new HaltCommand(identifier);
			}
		};								
		COMMANDS.put("H", h);
		COMMANDS.put("HALT", h);
		CommandFactory hh = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new HangCommand(identifier);
			}
		};										
		COMMANDS.put("H", hh);
		COMMANDS.put("HANG", hh);
		CommandFactory i = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new IfCommand(identifier);
			}
		};												
		COMMANDS.put("I", i);
		COMMANDS.put("IF", i);
		CommandFactory j = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new JobCommand(identifier);
			}
		};												
		COMMANDS.put("J", j);
		COMMANDS.put("JOB", j);
		CommandFactory k = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new KillCommand(identifier);
			}
		};												
		COMMANDS.put("K", k);
		COMMANDS.put("KILL", k);
		CommandFactory l = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new LockCommand(identifier);
			}
		};												
		COMMANDS.put("L", l);
		COMMANDS.put("LOCK", l);
		CommandFactory m = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new MergeCommand(identifier);
			}
		};												
		COMMANDS.put("M", m);
		COMMANDS.put("MERGE", m);
		CommandFactory n = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new NewCommand(identifier);
			}
		};												
		COMMANDS.put("N", n);
		COMMANDS.put("NEW", n);
		CommandFactory o = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new OpenCommand(identifier);
			}
		};												
		COMMANDS.put("O", o);
		COMMANDS.put("OPEN", o);
		CommandFactory q = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new QuitCommand(identifier);
			}
		};												
		COMMANDS.put("Q", q);
		COMMANDS.put("QUIT", q);
		CommandFactory r = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new ReadCommand(identifier);
			}
		};												
		COMMANDS.put("R", r);
		COMMANDS.put("READ", r);
		CommandFactory s = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new SetCommand(identifier);
			}
		};												
		COMMANDS.put("S", s);
		COMMANDS.put("SET", s);
		CommandFactory tc = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TCommitCommand(identifier);
			}
		};												
		COMMANDS.put("TC", tc);
		COMMANDS.put("TCOMMIT", tc);
		CommandFactory tr = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TRestartCommand(identifier);
			}
		};												
		COMMANDS.put("TR", tr);
		COMMANDS.put("TRESTART", tr);
		CommandFactory tro = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TRollbackCommand(identifier);
			}
		};												
		COMMANDS.put("TRO", tro);
		COMMANDS.put("TROLLBACK", tro);
		CommandFactory ts = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new TStartCommand(identifier);
			}
		};												
		COMMANDS.put("TS", ts);
		COMMANDS.put("TSTART", ts);
		CommandFactory u = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new UseCommand(identifier);
			}
		};												
		COMMANDS.put("U", u);
		COMMANDS.put("USE", u);
		CommandFactory v = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new ViewCommand(identifier);
			}
		};												
		COMMANDS.put("V", v);
		COMMANDS.put("VIEW", v);
		CommandFactory w = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new WriteCommand(identifier);
			}
		};												
		COMMANDS.put("W", w);
		COMMANDS.put("WRITE", w);
		CommandFactory x = new CommandFactory() {			
			@Override
			public TCommand getInstance(String identifier) {
				return new XecuteCommand(identifier);
			}
		};												
		COMMANDS.put("X", x);
		COMMANDS.put("XECUTE", x);
	}
	
	private boolean hasTagToken;
	private boolean hasLevelToken;
	
	@Override
	public List<MError> getErrors() {
		if (this.size() == 0) {
			MError[] errors = new MError[]{new MError(MError.ERR_NULL_LINE)};
			return Arrays.asList(errors);
		}
		return super.getErrors();
	}
	
	public int getLevel() {
		if (this.hasLevelToken) {
			int index = this.hasTagToken ? 1 : 0;
			IToken levelToken = this.get(index);
			String s = levelToken.getStringValue();
			int count = 0;
			for (int i=0; i<s.length(); ++i) {
				if (s.charAt(i) == '.') {
					++count;
				}
			}
			return count;
		}
		return 0;	
	}

	public String getTagView() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		if (this.hasTagToken) {
			IToken tagToken = this.get(0);
			++index;
			sb.append(tagToken.getStringValue());
		}
		int count = this.getLevel();		
		sb.append(" LEVEL:" + String.valueOf(count) + " ");
		if (this.hasLevelToken) ++index;
		if (this.size() > index) {
			sb.append(" ...");
		}
		return sb.toString();
	}

	public String getCommandView() {
		StringBuilder sb = new StringBuilder();
		if (this.hasTagToken) {
			IToken token = this.get(0);
			sb.append(token.getStringValue());
		}
		if (this.hasLevelToken) {
			IToken token = this.get(this.hasTagToken ? 1 : 0);
			sb.append(token.getStringValue());
		}
		sb.append(this.getCommandView());
		return sb.toString();
	}

	private static TCommand getCommand(String identifier) {
		CommandFactory f = COMMANDS.get(identifier.toUpperCase());
		if (f != null) {
			TCommand command = f.getInstance(identifier);
			return command;
		} else {
			TCommand command = new GenericCommand(identifier);
			return command;
		}
	}
	
	public static Line getInstance(String line) {
		Line result = new Line();
		int length = line.length();
		if (line.length() > 0) {
			int index = 0;
			
			char ch = line.charAt(0);
			if (Character.isLetterOrDigit(ch) || ch == '%') {
				Tag tag = Tag.getInstance(line, 0);
				if (tag != null) {
					result.add(tag);
					result.hasTagToken = true;				
					index += tag.getStringSize();
				}
			}
			
			if (index < length) {
				ch = line.charAt(index);
				if (ch == ';') {
					Comment cmt = new Comment(line.substring(index));
					index = length;
					result.add(cmt);
				} else if (ch != ' ') {
					FatalError fet = new FatalError(MError.ERR_GENERAL_SYNTAX, line, index);
					index = length;
					result.add(fet);
				} else {
					Level level = Level.getInstance(line, index);
					result.add(level);
					result.hasLevelToken = true;
					index += level.getStringSize();
				}
			}
	
			while (index < length) {
				ch = line.charAt(index);
				if (ch == ';') {
					Comment cmt = new Comment(line.substring(index+1));
					index = length;
					result.add(cmt);				
				} else if (! Character.isLetter(ch)) {
					TSyntaxError err = new TSyntaxError(line, index);
					index = length;
					result.add(err);
				} else {
					int toIndex = Algorithm.findOther(line, index, Algorithm.CharType.LETTER_DIGIT);
					String identifier = line.substring(index, toIndex);
					TCommand newToken = Line.getCommand(identifier);
					index = newToken.extractDetails(line, toIndex);
					result.add(newToken);
				}			
			}
		}
		return result;
	}
}
