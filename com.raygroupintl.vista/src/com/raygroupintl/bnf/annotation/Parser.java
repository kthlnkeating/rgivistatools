package com.raygroupintl.bnf.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.CharacterAdapter;
import com.raygroupintl.bnf.StringAdapter;
import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.TFCharacter;
import com.raygroupintl.bnf.TFConstant;
import com.raygroupintl.bnf.TFEmpty;
import com.raygroupintl.bnf.TFEnd;
import com.raygroupintl.bnf.TFList;
import com.raygroupintl.bnf.TList;
import com.raygroupintl.bnf.Text;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TFString;
import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFChoiceOnChar0th;
import com.raygroupintl.bnf.TFChoiceOnChar1st;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.SequenceAdapter;
import com.raygroupintl.charlib.AndPredicate;
import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.charlib.CharRangePredicate;
import com.raygroupintl.charlib.CharsPredicate;
import com.raygroupintl.charlib.DigitPredicate;
import com.raygroupintl.charlib.ExcludePredicate;
import com.raygroupintl.charlib.LetterPredicate;
import com.raygroupintl.charlib.OrPredicate;
import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.m.struct.IdentifierStartPredicate;

public class Parser {
	private static final Map<String, Predicate> PREDICATES = new HashMap<String, Predicate>();
	static {
		PREDICATES.put("letter", new LetterPredicate());
		PREDICATES.put("digit", new DigitPredicate());
		PREDICATES.put("idstart", new IdentifierStartPredicate());
	}
		
	private static class Triple<T extends TokenFactory, A extends Annotation> {
		public String name;
		public T factory;
		public A annotation;
		
		public Triple(String name, T factory, A annotation) {
			this.name = name;
			this.factory = factory;
			this.annotation = annotation;
		}
	}
	
	private static class ConstructorAsSequenceAdapter implements SequenceAdapter {					
		private Constructor<? extends Token> constructor;
		
		public ConstructorAsSequenceAdapter(Constructor<? extends Token> constructor) {
			this.constructor = constructor;
		}
		
		@Override
		public Token convert(Token[] tokens) {
			try {
				return (Token) this.constructor.newInstance((Object) tokens);
			} catch (Exception e) {
				return null;
			}
		}
	};
	
	private static class ConstructorAsCharactersAdapter implements StringAdapter {					
		private Constructor<? extends Token> constructor;
		
		public ConstructorAsCharactersAdapter(Constructor<? extends Token> constructor) {
			this.constructor = constructor;
		}
		
		@Override
		public Token convert(String value) {
			try {
				return (Token) this.constructor.newInstance((Object) value);
			} catch (Exception e) {
				return null;
			}
		}
	};
	
	private static class ConstructorAsCharacterAdapter implements CharacterAdapter {					
		private Constructor<? extends Token> constructor;
		
		public ConstructorAsCharacterAdapter(Constructor<? extends Token> constructor) {
			this.constructor = constructor;
		}
		
		@Override
		public Token convert(char value) {
			try {
				return (Token) this.constructor.newInstance((Object) value);
			} catch (Exception e) {
				return null;
			}
		}
	};
	
	private static class DLAdapter implements SequenceAdapter {
		@Override
		public Token convert(Token[] tokens) {
			if (tokens[1] == null) {
				return new TList(tokens[0]);	
			} else {		
				TList result = (TList) tokens[1];
				result.add(0, tokens[0]);
				return result;
			}			
		}
	}

	private static class Store {
		private static DescriptionSpec descriptionSpec;
		
		public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
		
		private java.util.List<Triple<TFChoiceBasic, Choice>> choices  = new ArrayList<Triple<TFChoiceBasic, Choice>>();
		private java.util.List<Triple<TFSequenceStatic, Sequence>> sequences  = new ArrayList<Triple<TFSequenceStatic, Sequence>>();
		private java.util.List<Triple<TFSequenceStatic, Description>> descriptions  = new ArrayList<Triple<TFSequenceStatic, Description>>();
		private java.util.List<Triple<TFList, List>> lists  = new ArrayList<Triple<TFList, List>>();
		private java.util.List<Triple<TFSequenceStatic, List>> enclosedLists  = new ArrayList<Triple<TFSequenceStatic, List>>();
		private java.util.List<Triple<TFSequenceStatic, List>> delimitedLists  = new ArrayList<Triple<TFSequenceStatic, List>>();
		private java.util.List<Triple<TFSequenceStatic, List>> enclosedDelimitedLists  = new ArrayList<Triple<TFSequenceStatic, List>>();
		private java.util.List<Triple<TFChoiceOnChar0th, CChoice>> choice0ths  = new ArrayList<Triple<TFChoiceOnChar0th, CChoice>>();
		private java.util.List<Triple<TFChoiceOnChar1st, CChoice>> choice1sts  = new ArrayList<Triple<TFChoiceOnChar1st, CChoice>>();
		private Map<String, Field> otherSymbols = new HashMap<String, Field>();
		
		private TokenFactory addChoice(String name, Choice choice) {
			TFChoiceBasic value = new TFChoiceBasic();
			this.choices.add(new Triple<TFChoiceBasic, Choice>(name, value, choice));
			return value;			
		}
		
		private StringAdapter getStringAdapter(Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Adapter adapter = f.getAnnotation(Adapter.class);
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				StringAdapter ta = (StringAdapter) cls.newInstance();
				return ta;
			}
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				Class<? extends Token> tokenCls = tokenType.value();
				Constructor<? extends Token> constructor = tokenCls.getConstructor(String.class);
				StringAdapter ta = new ConstructorAsCharactersAdapter(constructor);
				return ta;
			}
			return new DefaultStringAdapter();
		}

		private CharacterAdapter getCharacterAdapter(Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Adapter adapter = f.getAnnotation(Adapter.class);
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				CharacterAdapter ta = (CharacterAdapter) cls.newInstance();
				return ta;
			}
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				Class<? extends Token> tokenCls = tokenType.value();
				Constructor<? extends Token> constructor = tokenCls.getConstructor(char.class);
				CharacterAdapter ta = new ConstructorAsCharacterAdapter(constructor);
				return ta;
			}
			return new DefaultCharacterAdapter();
		}

		private boolean updateAdapter(TFSequenceStatic token, Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Adapter adapter = f.getAnnotation(Adapter.class);
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				SequenceAdapter ta = (SequenceAdapter) cls.newInstance();
				token.setAdapter(ta);
				return true;
			}
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				Class<? extends Token> tokenCls = tokenType.value();
				Constructor<? extends Token> constructor = tokenCls.getConstructor(Token[].class);
				SequenceAdapter ta = new ConstructorAsSequenceAdapter(constructor);
				token.setAdapter(ta);
				return true;
			}		
			return false;
		}
		
		private TokenFactory addSequence(String name, Sequence sequence, Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			TFSequenceStatic value = new TFSequenceStatic();
			this.updateAdapter(value, f);
			this.sequences.add(new Triple<TFSequenceStatic, Sequence>(name, value, sequence));
			return value;			
		}
		
		private TokenFactory addDescription(String name, Description description, Field f)  throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			TFSequenceStatic value = new TFSequenceStatic();
			this.updateAdapter(value, f);
			this.descriptions.add(new Triple<TFSequenceStatic, Description>(name, value, description));
			return value;		
		}
		
		private TokenFactory addList(String name, List list, Field f)  throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			String delimiter = list.delim();
			String left = list.left();
			String right = list.right();
			if (delimiter.length() == 0) {
				if ((left.length() == 0) || (right.length() == 0)) {
					TFList value = new TFList();
					this.lists.add(new Triple<TFList, List>(name, value, list));
					return value;
				} else {
					TFSequenceStatic value = new TFSequenceStatic();
					this.updateAdapter(value, f);
					this.enclosedLists.add(new Triple<TFSequenceStatic, List>(name, value, list));
					return value;
				}
			} else {			
				if ((left.length() == 0) || (right.length() == 0)) {
					TFSequenceStatic value = new TFSequenceStatic();
					if (! this.updateAdapter(value, f)) {
						value.setAdapter(new DLAdapter());				
					}
					this.delimitedLists.add(new Triple<TFSequenceStatic, List>(name, value, list));
					return value;
				} else {
					TFSequenceStatic value = new TFSequenceStatic();
					this.updateAdapter(value, f);
					this.enclosedDelimitedLists.add(new Triple<TFSequenceStatic, List>(name, value, list));
					return value;					
				}
			}
		}
		
		private TokenFactory addCChoice(String name, CChoice cchoice) {
			String lead = cchoice.lead();
			if (lead.length() == 0) {
				TFChoiceOnChar0th value = new TFChoiceOnChar0th();
				this.choice0ths.add(new Triple<TFChoiceOnChar0th, CChoice>(name, value, cchoice));
				return value;
			} else {
				TFChoiceOnChar1st value = new TFChoiceOnChar1st();
				this.choice1sts.add(new Triple<TFChoiceOnChar1st, CChoice>(name, value, cchoice));
				return value;
			}
		}
		
		private Predicate orPredicates(Predicate p0, Predicate p1) {
			if (p1 == null) return p0;
			if (p0 == null) return p1;
			return new OrPredicate(p0, p1);
		}

		private Predicate andPredicates(Predicate p0, Predicate p1) {
			if (p1 == null) return p0;
			if (p0 == null) return p1;
			return new AndPredicate(p0, p1);
		}
		
		private TokenFactory addCharacters(String name, CharSpecified characters, Field f)  throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Predicate p0 = getCharPredicate(characters.chars());
			Predicate p1 = getCharRanges(characters.ranges());
			Predicate p2 = getCharPredicate(characters.excludechars());
			if (p2 != null) {
				p2 = new ExcludePredicate(p2);
			}			
			Predicate p3 = getCharRanges(characters.excluderanges());
			if (p3 != null) {
				p3 = new ExcludePredicate(p3);
			}			
			Predicate result = andPredicates(orPredicates(p0, p1), orPredicates(p2, p3));
			if (characters.single()) {
				CharacterAdapter ca = this.getCharacterAdapter(f);
				TFCharacter tf = new TFCharacter(result, ca);
				return tf;
			} else {		
				StringAdapter sa = this.getStringAdapter(f);
				TFString tf = new TFString(result, sa);
				return tf;
			}
		}
		
		private TokenFactory addWords(String name, WordSpecified wordSpecied, Field f)  throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			String word = wordSpecied.value();
			StringAdapter sa = this.getStringAdapter(f);
			TFConstant tf = new TFConstant(word, sa, wordSpecied.ignorecase());
			return tf;
		}
		
		private TokenFactory add(Field f) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
			String name = f.getName();
			
			Choice choice = f.getAnnotation(Choice.class);
			if (choice != null) {
				return this.addChoice(name, choice);
			}			
			Sequence sequence = f.getAnnotation(Sequence.class);
			if (sequence != null) {
				return this.addSequence(name, sequence, f);
			}			
			Description description = f.getAnnotation(Description.class);
			if (description != null) {
				return this.addDescription(name, description, f);
			}			
			List list = f.getAnnotation(List.class);
			if (list != null) {
				return this.addList(name, list, f);
			}			
			CChoice cchoice = f.getAnnotation(CChoice.class);
			if (cchoice != null) {
				return this.addCChoice(name, cchoice);
			}		
			CharSpecified characters = f.getAnnotation(CharSpecified.class);
			if (characters != null) {
				return this.addCharacters(name, characters, f);
			}
			WordSpecified words = f.getAnnotation(WordSpecified.class);
			if (words != null) {
				return this.addWords(name, words, f);
			}
			return null;
		}
		
		public <T> void add(T target) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
			Class<?> cls = target.getClass();
			while (! cls.equals(Object.class)) {
				for (Field f : cls.getDeclaredFields()) {
					if (TokenFactory.class.isAssignableFrom(f.getType())) {
						String name = f.getName();
						TokenFactory already = this.symbols.get(name);
						if (already == null) {					
							TokenFactory value = (TokenFactory) f.get(target);
							if (value == null) {
								value = this.add(f);
								if (value != null) {
									f.set(target, value);
								} else {
									this.otherSymbols.put(name, f);
								}
							}
							if (value != null) {
								this.symbols.put(name, value);
							}
						} else {
							f.set(target, already);						
						}
					}
				}
				cls = cls.getSuperclass();
			}			
		}
		
		public <T> void updateEquivalents(T target) throws IllegalAccessException {
			for (String name : this.otherSymbols.keySet()) {			
				Field f = this.otherSymbols.get(name);
				Equivalent annot = f.getAnnotation(Equivalent.class);
				if (annot != null) {
					String source = annot.value();
					TokenFactory sourceFactory = this.symbols.get(source);
					f.set(target, sourceFactory);
					this.symbols.put(name, sourceFactory);
				}			
			}
		}

		private void updateChoices() {		
			for (Triple<TFChoiceBasic, Choice> p : this.choices) {
				TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
				p.factory.setFactories(fs);
			}
		}
		
		private void updateChoicesOnChar0th() {
			for (Triple<TFChoiceOnChar0th, CChoice> p : this.choice0ths) {
				TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
				Predicate[] ps = getPredicates(p.annotation.preds());
				p.factory.setChoices(ps, fs);
				String dcode = p.annotation.def();
				if (dcode.length() > 0) {
					TokenFactory df = this.symbols.get(dcode);
					p.factory.setDefault(df);
				}
			}
		}
		
		private void updateChoicesOnChar1st() {
			for (Triple<TFChoiceOnChar1st, CChoice> p : this.choice1sts) {
				TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
				Predicate[] ps = getPredicates(p.annotation.preds());
				p.factory.setLeadingChar(p.annotation.lead().charAt(0));
				p.factory.setChoices(ps, fs);
				String dcode = p.annotation.def();
				if (dcode.length() > 0) {
					TokenFactory df = this.symbols.get(dcode);
					p.factory.setDefault(df);
				}
			}
		}
		
		private void updateSequences() {
			for (Triple<TFSequenceStatic, Sequence> p : this.sequences) {
				TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
				p.factory.setFactories(fs);
				boolean[] required = getRequiredFlags(p.annotation.required(), fs.length);
				p.factory.setRequiredFlags(required);			
			}
		}
	
		private void updateDescription() throws ParseException {
			try {
				if (descriptionSpec == null) {
					Parser parser = new Parser();
					descriptionSpec = parser.parse(DescriptionSpec.class, true);
				}
				for (Triple<TFSequenceStatic, Description> p : this.descriptions) {
					String description = p.annotation.value();
					Text text = new Text(description, 0);
					TDescription token = (TDescription) descriptionSpec.description.tokenize(text);
					TFSequenceStatic f = (TFSequenceStatic) token.getFactory(this.symbols);
					p.factory.copyFrom(f);
				}
			} catch (SyntaxErrorException se) {
				throw new ParseException(se);
			}
		}

		private void updateLists() {
			for (Triple<TFList, List> p : this.lists) {
				TokenFactory f = this.symbols.get(p.annotation.value());
				p.factory.setElementFactory(f);
			}	
		}
		
		private void updateEnclosedLists() {
			for (Triple<TFSequenceStatic, List> p : this.enclosedLists) {
				TokenFactory e = this.symbols.get(p.annotation.value());
				TokenFactory l = this.symbols.get(p.annotation.left());
				TokenFactory r = this.symbols.get(p.annotation.right());
				TFList f = new TFList(e);
				p.factory.setFactories(new TokenFactory[]{l, f, r});
				p.factory.setRequiredFlags(new boolean[]{true, ! p.annotation.none(), true});
			}	
		}
		
		private void updateEnclosedDelimitedLists() {
			for (Triple<TFSequenceStatic, List> p : this.enclosedDelimitedLists) {
				TokenFactory e = this.symbols.get(p.annotation.value());
				TokenFactory d = this.symbols.get(p.annotation.delim());
				TokenFactory l = this.symbols.get(p.annotation.left());
				TokenFactory r = this.symbols.get(p.annotation.right());
				TokenFactory t = e;
				if (p.annotation.empty()) {
					TokenFactory eDelimiter = new TFEmpty(d);
					TokenFactory eRight = new TFEmpty(r);
					t = new TFChoiceBasic(e, eDelimiter, eRight);
				}
				TFSequenceStatic ts = new TFSequenceStatic(d, t);
				ts.setRequiredFlags(new boolean[]{true, true});
				TFList tail = new TFList(ts);
				
				TokenFactory leadingElement = e;
				if (p.annotation.empty()) {
					TokenFactory eDelimiter = new TFEmpty(d);
					leadingElement = new TFChoiceBasic(e, eDelimiter);
				}				
				TFSequenceStatic m = new TFSequenceStatic(leadingElement, tail);
				m.setRequiredFlags(new boolean[]{true, false});
				m.setAdapter(new DLAdapter());				
				p.factory.setFactories(new TokenFactory[]{l, m, r});
				p.factory.setRequiredFlags(new boolean[]{true, ! p.annotation.none(), true});
			}	
		}
		
		private void updateDelimitedLists() {
			for (Triple<TFSequenceStatic, List> p : this.delimitedLists) {
				TokenFactory e = this.symbols.get(p.annotation.value());
				TokenFactory d = this.symbols.get(p.annotation.delim());
				TokenFactory t = e;
				if (p.annotation.empty()) {
					TokenFactory eDelimiter = new TFEmpty(d);
					t = new TFChoiceBasic(e, eDelimiter);
				}
				TFSequenceStatic ts = new TFSequenceStatic(d, t);
				ts.setRequiredFlags(new boolean[]{true, true});
				TFList tail = new TFList(ts);
				
				TokenFactory leadingElement = e;
				if (p.annotation.empty()) {
					TokenFactory eDelimiter = new TFEmpty(d);
					leadingElement = new TFChoiceBasic(e, eDelimiter);
				}				
				p.factory.setFactories(new TokenFactory[]{leadingElement, tail});
				p.factory.setRequiredFlags(new boolean[]{true, false});
			}	
		}
		
		public void update(Class<?> cls, boolean ignore)  throws IllegalAccessException, InstantiationException, ParseException {
			this.updateChoices();
			this.updateChoicesOnChar0th();
			this.updateChoicesOnChar1st();
			this.updateSequences();
			this.updateLists();
			this.updateEnclosedLists();
			this.updateEnclosedDelimitedLists();
			this.updateDelimitedLists();
			if (! ignore) {
				this.updateDescription();
			}
		}		
	}
	
	private static Predicate getCharPredicate(char[] chs) {
		if (chs.length == 1) {
			return new CharPredicate(chs[0]);
		} else if (chs.length > 1) {
			return new CharsPredicate(chs);
		} else {
			return null;
		}		
	}
	
	private static Predicate getCharRanges(char[] chs) {
		Predicate result = null;
		for (int i=1; i<chs.length; i=i+2) {
			char ch0 = chs[i];
			char ch1 = chs[i-1];
			Predicate p =  new CharRangePredicate(ch0, ch1);
			if (result == null) {
				result = p;
			} else {
				result = new OrPredicate(result, p);
			}
		}
		return result;
	}
	
	private static TokenFactory[] getFactories(Map<String, TokenFactory> map, String[] names) {
		int n = names.length;
		TokenFactory[] fs = new TokenFactory[n];
		for (int i=0; i<n; ++i) {
			String name = names[i];
			fs[i] = map.get(name);
		}
		return fs;
	}
	
	private static boolean[] getRequiredFlags(String specification, int n) {
		boolean[] result = new boolean[n];
		if (specification.equals("all")) {
			Arrays.fill(result, true);
			return result;
		}
		if (specification.equals("none")) {
			return result;
		}
		for (int i=0; i<specification.length(); ++i) {
			char ch = specification.charAt(i);
			if (ch == 'r') {
				result[i] = true;
			}
		}
		return result;
	}
	
	private static Predicate[] getPredicates(String[] codes) {
		int n = codes.length;
		Predicate[] result = new Predicate[n];
		for (int i=0; i<n; ++i) {
			String code = codes[i];
			if (code.length() == 1) {
				result[i] = new CharPredicate(code.charAt(0));
			} else {
				Predicate named = PREDICATES.get(code);
				if (named == null) {
					result[i] = new CharsPredicate(code.toCharArray());	
				} else {
					result[i] = PREDICATES.get(code);
				}
			}
		}
		return result;
	}
		
	private <T> T parse(Class<T> cls, boolean ignore) throws ParseException {
		try {
			T target = cls.newInstance();
			Store store = new Store();
			store.add(target);
			store.symbols.put("end", new TFEnd());
			store.updateEquivalents(target);
			store.update(cls, ignore);
			return target;
		} catch (IllegalAccessException iae) {
			throw new ParseException(iae);
		} catch (InstantiationException ine) {
			throw new ParseException(ine);
		} catch (ClassNotFoundException cnf) {
			throw new ParseException(cnf);
		} catch (NoSuchMethodException nsm) {
			throw new ParseException(nsm);			
		}
	}

	public <T> T parse(Class<T> cls) throws ParseException {
		return this.parse(cls, false);
	}
}
