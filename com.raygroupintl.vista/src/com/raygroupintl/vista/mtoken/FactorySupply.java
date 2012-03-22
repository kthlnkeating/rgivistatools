package com.raygroupintl.vista.mtoken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.token.TList;
import com.raygroupintl.vista.token.TSyntaxError;


public class FactorySupply {
	private static class IndirectionFactory implements ITokenFactory {
		private static class ITFTokenizeStopPredicate implements Algorithm.StopPredicate {
			@Override
			public boolean evaluate(String line, int index) {
				char ch = line.charAt(index);
				if (ch == ' ') return true;
				if (ch == '@') {
					int length = line.length();
					int nextIndex = index + 1;
					if (nextIndex < length) {
						char nextCh = line.charAt(nextIndex);
						if (nextCh == '(') return true;
					}
				}
				return false;
			}
			
		}
		
		@Override
		public IToken tokenize(String line, int index) {
			int endIndex = line.length();
			if ((index >= endIndex) || line.charAt(index) != '@') {
				return null;
			}
						
			int notSpaceIndex = Algorithm.findOther(line, index+1, ' ');
			if (notSpaceIndex == endIndex) {
				return new TSyntaxError(line, index);
			}
			
			ITFTokenizeStopPredicate predicate = new ITFTokenizeStopPredicate();
			TList tokens = Algorithm.tokenize(line, notSpaceIndex, predicate);
			
			int stringSize = tokens.getStringSize();
			int toIndex = notSpaceIndex + stringSize;
			if (toIndex >= endIndex) {
				assert(toIndex == endIndex);
				return new TIndirection(notSpaceIndex-index-1, tokens);
			}
			
			char toChar = line.charAt(toIndex);
			if (toChar == ' ') {
				return new TIndirection(notSpaceIndex-index-1, tokens);
			}
			
			assert(toChar == '@');
			ITokenFactory lptf = FactorySupply.findFactory('(');
			IToken subscriptTokens = lptf.tokenize(line, toIndex+1);
			return new TIndirection(notSpaceIndex-index-1, tokens, subscriptTokens); 
		}

	}
	
	private static class ExtrinsicTF implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			assert(line.substring(index, index+2).equals("$$"));
			return new TBasic("$$");
		}
	}

	private static class ExternalTF implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			assert(line.substring(index, index+2).equals("$&"));
			return new TBasic("$&");
		}
	}

	private static class IntrinsicTF extends MSpecialTF {

		@Override
		protected MSpecial getMSpecialTokenInstance(String identifier, boolean hasLeftParanthesis) {
			if (hasLeftParanthesis) {
				return IntrinsicFunction.getInstance(identifier);			
			} else {		
				return IntrinsicVariable.getInstance(identifier);
			}
		}

		@Override
		protected String getPrefix() {
			return "$";
		}

		@Override
		protected int getPrefixSize() {
			return 1;
		}

	}
	

	private static class StructuredSystemVariableTF extends MSpecialTF {
	
		@Override
		protected MSpecial getMSpecialTokenInstance(String identifier, boolean hasLeftParanthesis) {
			return StructuredSystemVariable.getInstance(identifier);
		}
	
		@Override
		protected String getPrefix() {
			return "^$";
		}
	
		@Override
		protected int getPrefixSize() {
			return 2;
		}
	}
	
	private static class DollarTF implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			assert(line.charAt(index) == '$');
			int length = line.length();
			if (index < length) {
				char ch = line.charAt(index+1);
				if (ch == '&') {
					ITokenFactory factory = new ExternalTF();
					return factory.tokenize(line, index);
				}
				if (ch == '$') {
					ITokenFactory factory = new ExtrinsicTF();
					return factory.tokenize(line, index);					
				}
				if (Character.isLetter(ch)) {
					ITokenFactory factory = new IntrinsicTF();
					return factory.tokenize(line, index);					
				}
			}
			return FatalError.getInstance(MError.ERR_GENERAL_SYNTAX, line, index);
		}
	}

	private static class CaretTF implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			assert(line.charAt(index) == '^');
			int length = line.length();
			if (index < length) {
				char ch = line.charAt(index+1);
				if (ch == '$') {
					ITokenFactory factory = new StructuredSystemVariableTF();
					return factory.tokenize(line, index);
				}
				return new Caret();
			}
			return FatalError.getInstance(MError.ERR_GENERAL_SYNTAX, line, index);
		}		
	}

	private static class TCharacter extends Base {
		private char value;
		
		public TCharacter(char value) {
			this.value = value;
		}
		
		@Override
		public String getStringValue() {
			return String.valueOf(this.value);
		}
		
		@Override
		public int getStringSize() {
			return 1;
		}
		
		@Override
		public List<MError> getErrors() {
			return null;
		}

		@Override
		public void beautify() {			
		}
		
		@Override
		public boolean isError() {
			return false;
		}

	}
	
	private static class Plus extends TCharacter {	
		public Plus() {
			super('+');
		}
	}

	private static class Minus extends TCharacter {	
		public Minus() {
			super('-');
		}
	}

	private static class LeftSquareBracket extends TCharacter {	
		public LeftSquareBracket() {
			super('[');
		}
	}
	
	private static class RightSquareBracket extends TCharacter {	
		public RightSquareBracket() {
			super(']');
		}
	}
	
	private static class NumberSign extends TCharacter {	
		public NumberSign() {
			super('#');
		}
	}

	private static class EqualSign extends TCharacter {	
		public EqualSign() {
			super('=');
		}
	}

	private static class SingleQuote extends TCharacter {	
		public SingleQuote() {
			super('\'');
		}
	}
	
	private static class Slash extends TCharacter {	
		public Slash() {
			super('/');
		}
	}

	private static class Asterisk extends TCharacter {	
		public Asterisk() {
			super('*');
		}
	}
	
	private static class Underscore extends TCharacter {	
		public Underscore() {
			super('_');
		}
	}
	
	private static class Ampersand extends TCharacter {	
		public Ampersand() {
			super('&');
		}
	}
	
	private static class QuestionMark extends TCharacter {	
		public QuestionMark() {
			super('?');
		}
	}
	
	private static class ExclamationMark extends TCharacter {	
		public ExclamationMark() {
			super('!');
		}
	}
	
	private static class LessThanSign extends TCharacter {	
		public LessThanSign() {
			super('<');
		}
	}
	
	private static class MoreThanSign extends TCharacter {	
		public MoreThanSign() {
			super('>');
		}
	}
	
	private static class Colon extends TCharacter {	
		public Colon() {
			super(':');
		}
	}

	private static class Comma extends TCharacter {	
		public Comma() {
			super(',');
		}
	}

	private static class Percent extends TCharacter {	
		public Percent() {
			super('%');
		}
	}

	private static class Space extends TCharacter {	
		public Space() {
			super(' ');
		}
	}
	
	private static class Period extends TCharacter {	
		public Period() {
			super('.');
		}
	}
	
	private static class UnknownToken extends TCharacter {
		public UnknownToken(char ch) {
			super(ch);
		}
	}

	private static class Caret extends TCharacter {	
		public Caret() {
			super('^');
		}
	}
	
	private static class NumericLiteral extends TBasic {
		public NumericLiteral(String value) {
			super(value);
		}
	}
	
	private static abstract class PunctuationTokenFactory implements ITokenFactory {
		public abstract Base getToken();
		
		@Override
		public IToken tokenize(String line, int index) {
			Base token = this.getToken();
			return token;
		}
	}
	
	private static class RightParanthesisTokenFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			return FatalError.getInstance(MError.ERR_UNMATCHED_PARANTHESIS, line, index);
		}
	}

	private static class PlusTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Plus();
		}		
	}
	
	private static class MinusTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Minus();
		}		
	}
	
	private static class LeftBracketsTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new LeftSquareBracket();
		}		
	}
	
	private static class RightBracketsTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new RightSquareBracket();
		}		
	}

	private static class SingleQuoteTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new SingleQuote();
		}		
	}
	
	private static class NumberSignTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new NumberSign();
		}		
	}
	
	private static class EqualSignTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new EqualSign();
		}		
	}
	
	private static class ForwardSlashTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Slash();
		}		
	}
	
	private static class StarTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Asterisk();
		}		
	}
	
	
	private static class UnderscoreTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Underscore();
		}		
	}
	
	private static class AmpersandTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Ampersand();
		}		
	}
	
	private static class ExclamationMarkTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new ExclamationMark();
		}		
	}

	private static class QuestionMarkTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new QuestionMark();
		}		
	}
	
	private static class LessThanSignTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new LessThanSign();
		}		
	}
	
	private static class MoreThanSignTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new MoreThanSign();
		}		
	}

	private static class CommaTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Comma();
		}		
	}

	private static class ColonTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Colon();
		}		
	}

	private static class PercentTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Percent();
		}		
	}

	private static class SpaceTokenFactory extends PunctuationTokenFactory {
		@Override
		public Base getToken() {
			return new Space();
		}		
	}

	private static class UnknownTokenFactory  implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			UnknownToken token = new UnknownToken(line.charAt(index));
			return token;
		}
	}

	private static class LetterTokenFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			int fromIndex = index;
			++index;
			int length = line.length();
			while (index < length) {
				char ch = line.charAt(index);
				if (! Character.isLetterOrDigit(ch)) {
					return new Identifier(line.substring(fromIndex, index));
				}
				++index;
			} 
			return new Identifier(line.substring(fromIndex));
		}		
	}
	
	private static class DigitTokenFactory implements ITokenFactory {
		private static final int STATE_MANTISTA = 0;
		private static final int STATE_EXPONENT_START = 1;
		private static final int STATE_EXPONENT = 2;
		
		
		@Override
		public NumericLiteral tokenize(String line, int index) {
			int fromIndex = index;
			++index;
			int length = line.length();
			int state = STATE_MANTISTA;
			while (index < length) {				
				char ch = line.charAt(index);
				if (! (Character.isDigit(ch) || Character.isLetter(ch) || (ch == '.'))) {
					if (! ((state == STATE_EXPONENT_START) && ((ch == '-') || (ch == '+')))) {
						return new NumericLiteral(line.substring(fromIndex, index));
					}
				}
				if ((state == STATE_MANTISTA) && (ch == 'E')) {
					state = STATE_EXPONENT_START;
				} else if ((state == STATE_EXPONENT_START) && ((ch == '+') || (ch == '-'))) {
					state = STATE_EXPONENT;
				}
				++index;
			}
			return new NumericLiteral(line.substring(fromIndex));
		}		
	}

	private static class PeriodTokenFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			++index;
			int length = line.length();
			if (index < length) {
				char ch = line.charAt(index);
				if (Character.isDigit(ch)) {
					DigitTokenFactory factory = new DigitTokenFactory();
					NumericLiteral result = factory.tokenize(line, index);
					String value = result.getStringValue();
					value = '.' + value;
					NumericLiteral updatedResult = new NumericLiteral(value);
					return updatedResult;
				}
			}
			Period token = new Period();
			return token;			
		}		
	}
	
	private static class SemiColonTokenFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int index) {
			StringBuilder sb = new StringBuilder();
			int length = line.length();
			++index;
			while (index < length) {
				char ch = line.charAt(index);
				++index;
				sb.append(ch);
			}
			return new Comment(sb.toString());
		}		
	}

	private static class LeftParanthesisTF implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int index = fromIndex;
			++index;
			Algorithm ta = new Algorithm();
			ta.setStopChar(')');
			TList tokens = ta.tokenize(line, index);
			int length = line.length();
			int resultSize = tokens.getStringSize(); 
			index += resultSize; 
			if (index == length) {
				return FatalError.getInstance(MError.ERR_UNMATCHED_PARANTHESIS, line, fromIndex);
			}
			return new TInParantheses(tokens);
		}
	}

	private static abstract class MSpecialTF implements ITokenFactory {
		private IToken getErrorToken(String identifier, String line, int identifierToIndex) {
			TBasic it = new TBasic(identifier);
			String rest = line.substring(identifierToIndex); 
			TBasic st = new TBasic(rest);
			FatalError et = new FatalError(MError.ERR_GENERAL_SYNTAX, it, st);
			return et;		
		}
		
		protected IToken getMSpecialToken(String identifier, String line, int identifierToIndex) {
			int endIndex = line.length();
			boolean hasLeftParanthesis = false;
			if (identifierToIndex < endIndex) {
				hasLeftParanthesis = (line.charAt(identifierToIndex) == '(');
			}		
			MSpecial token = this.getMSpecialTokenInstance(identifier, hasLeftParanthesis);
			if (token != null) {
				if (token.hasArgument()) {
					if ((identifierToIndex >= line.length()) || line.charAt(identifierToIndex) != '(') {
						return this.getErrorToken(identifier, line, identifierToIndex);					
					}
					ITokenFactory f = FactorySupply.findFactory('(');
					IToken arguments = f.tokenize(line, identifierToIndex);
					if (arguments instanceof FatalError) {
						((FatalError) arguments).add(0, token);
						return arguments;
					}
					token.setArguments(arguments);
				}
				return token;
			} else {
				return this.getErrorToken(this.getPrefix()+identifier, line, identifierToIndex);
			}
		}

		protected abstract MSpecial getMSpecialTokenInstance(String identifier, boolean hasLeftParanthesis);
		
		protected abstract int getPrefixSize();
		
		protected abstract String getPrefix();
		
		@Override
		public IToken tokenize(String line, int index) {
			int fromIndex = index + this.getPrefixSize();
			Algorithm ta = new Algorithm();
			ta.setContinueCharType(Algorithm.CharType.LETTER_DIGIT);
			int toIndex = ta.find(line, fromIndex);
			String identifier = line.substring(fromIndex, toIndex);
			return this.getMSpecialToken(identifier, line, toIndex);
		}
	}
	
	private static final Map<Character, ITokenFactory> TOKEN_FACTORIES;
	static {
		TOKEN_FACTORIES = new HashMap<Character, ITokenFactory>();
		TOKEN_FACTORIES.put(')', new RightParanthesisTokenFactory());
		TOKEN_FACTORIES.put('+', new PlusTokenFactory());
		TOKEN_FACTORIES.put('-', new MinusTokenFactory());
		TOKEN_FACTORIES.put('[', new LeftBracketsTokenFactory());
		TOKEN_FACTORIES.put(']', new RightBracketsTokenFactory());
		TOKEN_FACTORIES.put('\'', new SingleQuoteTokenFactory());
		TOKEN_FACTORIES.put('/', new ForwardSlashTokenFactory());
		TOKEN_FACTORIES.put('*', new StarTokenFactory());
		TOKEN_FACTORIES.put('#', new NumberSignTokenFactory());
		TOKEN_FACTORIES.put('=', new EqualSignTokenFactory());
		TOKEN_FACTORIES.put('_', new UnderscoreTokenFactory());
		TOKEN_FACTORIES.put('&', new AmpersandTokenFactory());
		TOKEN_FACTORIES.put('?', new QuestionMarkTokenFactory());
		TOKEN_FACTORIES.put('!', new ExclamationMarkTokenFactory());
		TOKEN_FACTORIES.put('<', new LessThanSignTokenFactory());
		TOKEN_FACTORIES.put('>', new MoreThanSignTokenFactory());
		TOKEN_FACTORIES.put('@', new IndirectionFactory());
		TOKEN_FACTORIES.put(':', new ColonTokenFactory());
		TOKEN_FACTORIES.put(',', new CommaTokenFactory());
		
		TOKEN_FACTORIES.put('%', new PercentTokenFactory());
		TOKEN_FACTORIES.put(' ', new SpaceTokenFactory());
		
		TOKEN_FACTORIES.put('.', new PeriodTokenFactory());
		TOKEN_FACTORIES.put('"', new TFStringLiteral());
		TOKEN_FACTORIES.put(';', new SemiColonTokenFactory());
		TOKEN_FACTORIES.put('$', new DollarTF());
		TOKEN_FACTORIES.put('^', new CaretTF());
		TOKEN_FACTORIES.put('(', new LeftParanthesisTF());
	}
	
	private static boolean isDigit(char ch) {
		return (ch >= '0') && (ch <= '9');	
	}
	
	private static boolean isIdent(char ch) {
		return ((ch >= 'a') && (ch <='z')) || ((ch >= 'A') && (ch <='Z'));	
	}
	
	private static class NameFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			if (fromIndex < endIndex) {
				char ch = line.charAt(fromIndex);
				if ((ch == '%') || isIdent(ch)) {
					int index = fromIndex + 1;
					while (index < endIndex) {
						if(! (isDigit(ch) || isIdent(ch))) {
							break;
						}
					}
					String value = line.substring(fromIndex, index);
					return new TName(value);
				}
			}
			return null;			
		}
	}
	
	private static class RoutineNameFactory extends NameFactory {}
	
	private static class IntlitFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			int index = fromIndex;
			while (index < endIndex) {
				char ch = line.charAt(index);
				if(! isDigit(ch)) {
					break;
				}
			}
			if (fromIndex < index) {
				String value = line.substring(fromIndex, index);
				return new Identifier(value);				
			} else {			
				return null;
			}
		}
	}
	
	private static class FactoryOrFactory implements ITokenFactory {
		private ITokenFactory factory1;
		private ITokenFactory factory2;
		
		public FactoryOrFactory(ITokenFactory factory1, ITokenFactory factory2) {
			this.factory1 = factory1;
			this.factory2 = factory2;
		}
		
		@Override
		public IToken tokenize(String line, int fromIndex) {
			IToken result = this.factory1.tokenize(line, fromIndex);
			if (result == null) {
				return this.factory2.tokenize(line, fromIndex);				
			} else {
				return result;
			}
		}
	}
	
	private static class EnvironmentFactory implements ITokenFactory {
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			if (fromIndex < endIndex) {
				char ch = line.charAt(fromIndex);
				if (ch == '|') {
					int index = fromIndex + 1;
					if (index < endIndex) {
						TList environment = Algorithm.tokenize(line, index, '|');
						index += environment.getStringSize();
						if ((index < endIndex)) {
							return new TEnvironment(environment);
						} else {
							return new TSyntaxError(line, fromIndex);
						}
					}
				}
			}
			return null;			
		}
	}
		
	private static class OptionalFirstTwoFactories implements ITokenFactory {
		private ITokenFactory mainFactory;
		private ITokenFactory optionalFactory;
		
		public OptionalFirstTwoFactories(ITokenFactory mainFactory, ITokenFactory optionalFactory) {
			this.mainFactory = mainFactory;
			this.optionalFactory = optionalFactory;
		}
				
		@Override
		public IToken tokenize(String line, int fromIndex) {
			int endIndex = line.length();
			int index = fromIndex;

			IToken optionalToken = this.optionalFactory.tokenize(line, fromIndex);
			if (optionalToken != null) {
				if (optionalToken instanceof TSyntaxError) {
					return optionalToken;
				}
				index += optionalToken.getStringSize();
				if (index >= endIndex) {
					return new TSyntaxError(line, fromIndex);
				}
			}
			
			IToken mainToken = this.mainFactory.tokenize(line, index);
			if (mainToken == null) {
				return null;
			}
			if (mainToken instanceof TSyntaxError) {
				return new TSyntaxError(line, fromIndex);
			}
			
			if (optionalToken == null) {
				return mainToken;
			} else {
				return new TList(optionalToken, mainToken);				
			}
		}		
	}

	private static ITokenFactory Or(String key1, String key2) {
		ITokenFactory factory1 = FactorySupply.findFactory(key1);
		ITokenFactory factory2 = FactorySupply.findFactory(key2);
		return new FactoryOrFactory(factory1, factory2);
	}
	
	private static ITokenFactory Or(ITokenFactory factory1, String key2) {
		ITokenFactory factory2 = FactorySupply.findFactory(key2);
		return new FactoryOrFactory(factory1, factory2);
	}

	public static ITokenFactory OptionalMain(String mainKey, String optionalKey) {
		ITokenFactory mainFactory = FactorySupply.findFactory(mainKey);
		ITokenFactory optionalFactory = FactorySupply.findFactory(optionalKey);
		return new OptionalFirstTwoFactories(mainFactory, optionalFactory);
	}
	
	private static final Map<String, ITokenFactory> FACTORIES;
	static {
		FACTORIES = new HashMap<String, ITokenFactory>();
		FACTORIES.put("name", new NameFactory());
		FACTORIES.put("routinename", new RoutineNameFactory());
		FACTORIES.put("intlit", new IntlitFactory());
		FACTORIES.put("label", Or("name", "intlit"));
		FACTORIES.put("@", new IndirectionFactory());
		FACTORIES.put("dlabel", Or("label", "@"));
		FACTORIES.put("environment", new EnvironmentFactory());
		FACTORIES.put("routineref", Or(OptionalMain("environment", "routinename"), "@"));
	}
		
	public static ITokenFactory findFactory(char ch) {
		if (Character.isLetter(ch)) {
			return new LetterTokenFactory();
		}
		
		if (Character.isDigit(ch)) {
			return new DigitTokenFactory();
		}
		ITokenFactory tokenFactory = TOKEN_FACTORIES.get(ch);
		if (tokenFactory != null) {
			return tokenFactory;
		}
		return new UnknownTokenFactory();
	}
	
	public static ITokenFactory findFactory(String name) {
		ITokenFactory tokenFactory = FACTORIES.get(name);
		if (tokenFactory != null) {
			return tokenFactory;
		}
		char ch = name.charAt(0);
		return FactorySupply.findFactory(ch);
	}
}
