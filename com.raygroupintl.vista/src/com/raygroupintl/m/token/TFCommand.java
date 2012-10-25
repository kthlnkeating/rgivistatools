package com.raygroupintl.m.token;

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.ElseCmd;
import com.raygroupintl.m.parsetree.ForLoop;
import com.raygroupintl.m.parsetree.Goto;
import com.raygroupintl.m.parsetree.IfCmd;
import com.raygroupintl.m.parsetree.KillCmdNodes;
import com.raygroupintl.m.parsetree.MergeCmdNodes;
import com.raygroupintl.m.parsetree.NewCmdNodes;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.Nodes;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.m.parsetree.QuitCmd;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.SetCmdNodes;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.MNameWithMnemonic;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFEmptyVerified;
import com.raygroupintl.parser.TFSequence;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ObjectSupply;

public class TFCommand extends TokenFactory<MToken> {
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
		public MToken tokenize(Text text, ObjectSupply<MToken> objectSupply) {
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
	
	private abstract class TCommandSpec extends MCommand {
		private TokenFactory<MToken> argumentFactory;
		
		public TCommandSpec(MToken name, TokenFactory<MToken> argumentFactory) {
			super();
			this.addToken(name);
			this.argumentFactory = argumentFactory;
		}

		public TokenFactory<MToken> getArgumentFactory() {
			return this.argumentFactory;
		}

		public MToken tokenizeWhatFollows(Text text, ObjectSupply<MToken> objectSupply) throws SyntaxErrorException {
			TokenFactory<MToken> argumentFactory = this.getArgumentFactory();
			TFSequence<MToken> tf = new TFCommandRest(TFCommand.this.getName());
			tf.add(TFCommand.this.supply.postcondition, false);
			tf.add(TFCommand.this.supply.space, false);
			tf.add(argumentFactory, false);
			tf.add(TFCommand.this.supply.commandend, false);
			MToken nextToken = tf.tokenize(text, objectSupply);
			this.addToken(nextToken);
			return this;
		}
	}
		
	private class TBCommandSpec extends TCommandSpec {
		private TBCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		@Override
		protected String getFullName() {		
			return "BREAK";
		}			
	}
	
	private class TCCommandSpec extends TCommandSpec {
		private TCCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.closearg);
		}
	
		@Override
		protected String getFullName() {		
			return "CLOSE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.CloseCmd(postConditionNode, argumentNode);				
		}
	}
	
	private class TDCommandSpec extends TCommandSpec {
		private TDCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.doarguments);
		}
	
		@Override
		protected String getFullName() {		
			return "DO";
		}			

		@Override
		public Node getNode() {
			Node postConditionNode = this.getPostConditionNode();
			Node argumentNode = this.getArgumentNode();
			if (argumentNode == null) {
				return new DoBlock(postConditionNode);
			} else {
				Do result = new Do(postConditionNode, argumentNode);
				return result;
			}
		}
	}
	
	private class TECommandSpec extends TCommandSpec {
		private TECommandSpec(MToken value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		@Override
		protected String getFullName() {		
			return "ELSE";
		}			
		
		@Override
		public Node getNode() {
			return new ElseCmd();
		}			
	}

	private class TFCommandSpec extends TCommandSpec {
		private TFCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.forarg);
		}
	
		@Override
		protected String getFullName() {		
			return "FOR";
		}			

		@Override
		public Node getNode() {
			MToken argument = this.getArgument();
			if (argument == null) {
				return new ForLoop();
			} else {
				return new ForLoop(argument.getSubNode(0), argument.getSubNode(2));
			}
		}	
	}

	private class TGCommandSpec extends TCommandSpec {
		private TGCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.gotoarguments);
		}
	
		@Override
		protected String getFullName() {		
			return "GOTO";
		}	
		
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new Goto(postConditionNode, argumentNode);	
		}
	}

	private class THCommandSpec extends TCommandSpec {
		private THCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		@Override
		protected String getFullName() {
			Token argument = this.getToken(3);
			if (argument == null) {
				return "HALT";
			} else {
				return "HANG";
			}
		}			
	}

	private class TICommandSpec extends TCommandSpec {
		private TICommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.exprlist);
		}
	
		@Override
		protected String getFullName() {		
			return "IF";
		}			

		@Override
		public Node getNode() {
			Tokens<MToken> argument = this.getTokens(1, 2);
			if (argument != null) {
				Nodes<Node> node = NodeUtilities.getNodes(argument.toLogicalIterable(), argument.size());
				return new IfCmd(node);
			} else {
				return new IfCmd();
			}
		}			
	}

	private class TJCommandSpec extends TCommandSpec {
		private TJCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.cmdjargs);
		}
	
		@Override
		protected String getFullName() {		
			return "JOB";
		}			
	}

	private class TKCommandSpec extends TCommandSpec {
		private TKCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.killargs);
		}
	
		@Override
		protected String getFullName() {		
			return "KILL";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			if (argumentNode == null) {
				return new KillCmdNodes.AllKillCmd(postConditionNode);
			} else {
				return new KillCmdNodes.KillCmd(postConditionNode, argumentNode);				
			}
		}
	}

	private class TLCommandSpec extends TCommandSpec {
		private TLCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.lockargs);
		}
	
		@Override
		protected String getFullName() {		
			return "LOCK";
		}			
	}

	private class TMCommandSpec extends TCommandSpec {
		private TMCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.mergeargs);
		}
	
		@Override
		protected String getFullName() {		
			return "MERGE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new MergeCmdNodes.MergeCmd(postConditionNode, argumentNode);	
		}
	}

	private class TNCommandSpec extends TCommandSpec {
		private TNCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.newargs);
		}
	
		@Override
		protected String getFullName() {		
			return "NEW";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			if (argumentNode == null) {
				return new NewCmdNodes.AllNewCmd(postConditionNode);
			} else {
				return new NewCmdNodes.NewCmd(postConditionNode, argumentNode);				
			}
		}
	}

	private class TOCommandSpec extends TCommandSpec {
		private TOCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.cmdoargs);
		}
	
		@Override
		protected String getFullName() {		
			return "OPEN";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.OpenCmd(postConditionNode, argumentNode);				
		}
	}

	private class TQCommandSpec extends TCommandSpec {
		private TQCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.expr);
		}
	
		@Override
		protected String getFullName() {		
			return "QUIT";
		}			

		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new QuitCmd(postConditionNode, argumentNode);	
		}
	}

	private class TRCommandSpec extends TCommandSpec {
		private TRCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.cmdrargs);
		}
	
		@Override
		protected String getFullName() {		
			return "READ";
		}			

		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new ReadCmd(postConditionNode, argumentNode);	
		}
	}

	private class TSCommandSpec extends TCommandSpec {
		private TSCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.setargs);
		}
	
		@Override
		protected String getFullName() {		
			return "SET";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new SetCmdNodes.SetCmd(postConditionNode, argumentNode);	
		}
	}

	private class TTCCommandSpec extends TCommandSpec {
		private TTCCommandSpec(MToken value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		@Override
		protected String getFullName() {		
			return "TCOMMIT";
		}			
	}

	private class TTRCommandSpec extends TCommandSpec {
		private TTRCommandSpec(MToken value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		@Override
		protected String getFullName() {		
			return "TRESTART";
		}			
	}

	private class TTROCommandSpec extends TCommandSpec {
		private TTROCommandSpec(MToken value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		@Override
		protected String getFullName() {		
			return "TROLLBACK";
		}			
	}

	private class TTSCommandSpec extends TCommandSpec {
		private TTSCommandSpec(MToken value, MTFSupply supply) {
			super(value, TF_EMPTY);
		}
	
		@Override
		protected String getFullName() {		
			return "TSTART";
		}			
	}

	private class TUCommandSpec extends TCommandSpec {
		private TUCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.cmduargs);
		}
	
		@Override
		protected String getFullName() {		
			return "USE";
		}
		
		@Override
		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new OpenCloseUseCmdNodes.UseCmd(postConditionNode, argumentNode);				
		}
	}

	private class TWCommandSpec extends TCommandSpec {
		private TWCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.writeargs);
		}
	
		@Override
		protected String getFullName() {		
			return "WRITE";
		}			

		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new WriteCmd(postConditionNode, argumentNode);	
		}
	}

	private class TVCommandSpec extends TCommandSpec {
		private TVCommandSpec(MToken value, MTFSupply supply) {
			super(value, new TFGenericArgument("vargument"));
		}
		
		@Override
		protected String getFullName() {		
			return "VIEW";
		}			
	}

	private class TXCommandSpec extends TCommandSpec {
		private TXCommandSpec(MToken value, MTFSupply supply) {
			super(value, supply.xecuteargs);
		}
	
		@Override
		protected String getFullName() {		
			return "XECUTE";
		}			

		protected Node getNode(Node postConditionNode, Node argumentNode) {
			return new XecuteCmd(postConditionNode, argumentNode);	
		}
	}

	private class TGenericCommandSpec extends TCommandSpec {
		private MNameWithMnemonic mnwm;
		
		private TGenericCommandSpec(MToken value, MNameWithMnemonic mnwm, MTFSupply supply) {
			super(value, new TFGenericArgument("genericargument"));
			this.mnwm = mnwm;
		}
	
		@Override
		protected String getFullName() {		
			return this.toValue().toString();
		}					
	}
	
	private static abstract class TCSFactory {
		public abstract TCommandSpec get(MToken name);
	}
	
	public void addCommands(final MTFSupply supply) {
		TCSFactory b = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TBCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("B", b);
		this.commandSpecs.put("BREAK", b); 	
		
		TCSFactory c = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("C", c);
		this.commandSpecs.put("CLOSE", c); 	
		
		TCSFactory d = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TDCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("D", d);
		this.commandSpecs.put("DO", d); 	
		
		TCSFactory e = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TECommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("E", e);
		this.commandSpecs.put("ELSE", e); 	

		TCSFactory f = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TFCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("F", f);
		this.commandSpecs.put("FOR", f); 	
		
		TCSFactory g = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TGCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("G", g);
		this.commandSpecs.put("GOTO", g); 	
		
		TCSFactory h = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new THCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("H", h);
		this.commandSpecs.put("HALT", h); 	
		this.commandSpecs.put("HANG", h); 
		
		TCSFactory i = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TICommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("I", i);
		this.commandSpecs.put("IF", i); 	
		
		TCSFactory j = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TJCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("J", j);
		this.commandSpecs.put("JOB", j); 	
		
		TCSFactory k = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TKCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("K", k);
		this.commandSpecs.put("KILL", k); 	
		
		TCSFactory l = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TLCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("L", l);
		this.commandSpecs.put("LOCK", l); 	
		
		TCSFactory m = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TMCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("M", m);
		this.commandSpecs.put("MERGE", m); 
		
		TCSFactory n = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TNCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("N", n);
		this.commandSpecs.put("NEW", n);		
		
		TCSFactory o = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TOCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("O", o);
		this.commandSpecs.put("OPEN", o); 	
		
		TCSFactory q = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TQCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("Q", q);
		this.commandSpecs.put("QUIT", q); 	
		
		TCSFactory r = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("R", r);
		this.commandSpecs.put("READ", r); 	
		
		TCSFactory s = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("S", s);
		this.commandSpecs.put("SET", s); 	
		
		TCSFactory tc = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TTCCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TC", tc);
		this.commandSpecs.put("TCOMMIT", tc); 	
		
		TCSFactory tr = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TTRCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TR", tr);
		this.commandSpecs.put("TRESTART", tr); 	
		
		TCSFactory tro = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TTROCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TRO", tro);
		this.commandSpecs.put("TROLLBACK", tro); 	
		
		TCSFactory ts = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TTSCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("TS", ts);
		this.commandSpecs.put("TSTART", ts); 	
		
		TCSFactory u = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TUCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("U", u);
		this.commandSpecs.put("USE", u);
		
		TCSFactory v = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TVCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("V", v);
		this.commandSpecs.put("VIEW", v); 	
		
		TCSFactory w = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TWCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("W", w);
		this.commandSpecs.put("WRITE", w);	
		
		TCSFactory x = new TCSFactory() {			
			@Override
			public TCommandSpec get(MToken name) {
				return new TXCommandSpec(name, supply);
			}
		};
		this.commandSpecs.put("X", x);
		this.commandSpecs.put("XECUTE", x);		
	}
	
	public void addCommand(final String cmdName, final MTFSupply supply) {
		TCSFactory generic = new TCSFactory() {		
			@Override
			public TCommandSpec get(MToken name) {
				return new TGenericCommandSpec(name, new MNameWithMnemonic(cmdName, cmdName), supply);
			}
		};
		this.commandSpecs.put(cmdName, generic);
	}
	
	public void addCommand(final String mnemonic, final String cmdName, final MTFSupply supply) {
		TCSFactory generic = new TCSFactory() {		
			@Override
			public TCommandSpec get(MToken name) {
				return new TGenericCommandSpec(name, new MNameWithMnemonic(mnemonic, cmdName), supply);
			}
		};
		this.commandSpecs.put(mnemonic, generic);
		this.commandSpecs.put(cmdName, generic);
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
		public TFCommandName(String name) {
			super(name);
		}
		
		@Override
		public TCommandSpec tokenize(Text text, ObjectSupply<MToken> objectSupply) throws SyntaxErrorException {
			MToken token = TFCommand.this.supply.ident.tokenize(text, objectSupply);
			if (token != null) {
				TextPiece cmdName = token.toValue();
				TCSFactory tcs = TFCommand.this.commandSpecs.get(cmdName.toString().toUpperCase());
				if (tcs == null) {
					throw new SyntaxErrorException(MError.ERR_UNDEFINED_COMMAND);					
				} else {
					TCommandSpec spec = tcs.get(token);
					return spec;
				}
			}
			return null;
		}
	}
	
	@Override
	public MToken tokenize(Text text, ObjectSupply<MToken> objectSupply) {
		Text textCopy = text.getCopy();
		try {
			TFCommandName cmdSpecFactory = this.new TFCommandName("command.name");
			TCommandSpec cmdSpec = cmdSpecFactory.tokenize(text, objectSupply);
			return (cmdSpec == null) ? null : cmdSpec.tokenizeWhatFollows(text, objectSupply);
		} catch (SyntaxErrorException e) {
			int errorIndex = text.getIndex();
			int lengthToEOL = textCopy.findEOL();
			TextPiece t = textCopy.extractPiece(lengthToEOL);
			text.copyFrom(textCopy);
			return new MSyntaxError(e.getCode(), t, errorIndex);
		}
	}
}
