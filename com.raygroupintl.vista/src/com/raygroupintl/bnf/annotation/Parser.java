package com.raygroupintl.bnf.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.CharactersAdapter;
import com.raygroupintl.bnf.TFList;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TFCharacters;
import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFChoiceOnChar0th;
import com.raygroupintl.bnf.TFChoiceOnChar1st;
import com.raygroupintl.bnf.TFDelimitedList;
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
	
	private static class ConstructorAsCharactersAdapter implements CharactersAdapter {					
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
	
	private static class Store {
		private static DescriptionSpec descriptionSpec;
		
		public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
		
		private java.util.List<Triple<TFChoiceBasic, Choice>> choices  = new ArrayList<Triple<TFChoiceBasic, Choice>>();
		private java.util.List<Triple<TFSequenceStatic, Sequence>> sequences  = new ArrayList<Triple<TFSequenceStatic, Sequence>>();
		private java.util.List<Triple<TFSequenceStatic, Description>> descriptions  = new ArrayList<Triple<TFSequenceStatic, Description>>();
		private java.util.List<Triple<TFList, List>> lists  = new ArrayList<Triple<TFList, List>>();
		private java.util.List<Triple<TFDelimitedList, List>> delimitedLists  = new ArrayList<Triple<TFDelimitedList, List>>();
		private java.util.List<Triple<TFChoiceOnChar0th, CChoice>> choice0ths  = new ArrayList<Triple<TFChoiceOnChar0th, CChoice>>();
		private java.util.List<Triple<TFChoiceOnChar1st, CChoice>> choice1sts  = new ArrayList<Triple<TFChoiceOnChar1st, CChoice>>();
		private Map<String, Field> otherSymbols = new HashMap<String, Field>();
		
		private TokenFactory addChoice(String name, Choice choice) {
			TFChoiceBasic value = new TFChoiceBasic();
			this.choices.add(new Triple<TFChoiceBasic, Choice>(name, value, choice));
			return value;			
		}
		
		private void updateAdapter(TFCharacters token, Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Adapter adapter = f.getAnnotation(Adapter.class);
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				CharactersAdapter ta = (CharactersAdapter) cls.newInstance();
				token.setAdapter(ta);
				return;
			}
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				Class<? extends Token> tokenCls = tokenType.value();
				Constructor<? extends Token> constructor = tokenCls.getConstructor(String.class);
				CharactersAdapter ta = new ConstructorAsCharactersAdapter(constructor);
				token.setAdapter(ta);
				return;
			}			
		}

		private void updateAdapter(TFSequenceStatic token, Field f) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
			Adapter adapter = f.getAnnotation(Adapter.class);
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				SequenceAdapter ta = (SequenceAdapter) cls.newInstance();
				token.setAdapter(ta);
				return;
			}
			TokenType tokenType = f.getAnnotation(TokenType.class);
			if (tokenType != null) {
				Class<? extends Token> tokenCls = tokenType.value();
				Constructor<? extends Token> constructor = tokenCls.getConstructor(Token[].class);
				SequenceAdapter ta = new ConstructorAsSequenceAdapter(constructor);
				token.setAdapter(ta);
				return;
			}			
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
		
		private TokenFactory addList(String name, List list) {
			String delimiter = list.delim();
			String left = list.left();
			String right = list.right();
			if (delimiter.length() == 0) {
				if ((left.length() == 0) || (right.length() == 0)) {
					TFList value = new TFList();
					this.lists.add(new Triple<TFList, List>(name, value, list));
					return value;
				}				
			}			
			TFDelimitedList value = new TFDelimitedList();
			this.delimitedLists.add(new Triple<TFDelimitedList, List>(name, value, list));
			return value;			
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
		
		private TokenFactory addCharacters(String name, Characters characters, Field f)  throws IllegalAccessException, InstantiationException, NoSuchMethodException {
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
			Predicate[] ps = {p0, p1, p2, p3};
			Predicate result = null;
			for (Predicate p : ps) {
				if (p != null) {
					if (result == null) {
						result = p;
					} else {
						result = new AndPredicate(result, p);
					}
				}
			}
			TFCharacters tf = new TFCharacters(result);
			this.updateAdapter(tf, f);
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
				return this.addList(name, list);
			}			
			CChoice cchoice = f.getAnnotation(CChoice.class);
			if (cchoice != null) {
				return this.addCChoice(name, cchoice);
			}		
			Characters characters = f.getAnnotation(Characters.class);
			if (characters != null) {
				return this.addCharacters(name, characters, f);
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
			if (descriptionSpec == null) {
				Parser parser = new Parser();
				descriptionSpec = parser.parse(DescriptionSpec.class);
			}
			for (Triple<TFSequenceStatic, Description> p : this.descriptions) {
				//TokenFactory[] fs = getFactories(this.symbols, p.annotation.value());
				//p.factory.setFactories(fs);
				//boolean[] required = getRequiredFlags(p.annotation.required(), fs.length);
				//p.factory.setRequiredFlags(required);			
			}
		}

		private void updateLists() {
			for (Triple<TFList, List> p : this.lists) {
				TokenFactory f = this.symbols.get(p.annotation.value());
				p.factory.setElementFactory(f);
				p.factory.setAddErrorToList(p.annotation.adderror());
			}	
		}
		
		private void updateDelimitedLists() {
			for (Triple<TFDelimitedList, List> p : this.delimitedLists) {
				TokenFactory f = this.symbols.get(p.annotation.value());
				p.factory.setElementFactory(f);
				String delim = p.annotation.delim();
				if (delim.length() > 0) {
					TokenFactory d = this.symbols.get(delim);
					p.factory.setDelimiter(d);
				}
				String left = p.annotation.left();
				if (left.length() > 0) {
					TokenFactory l = this.symbols.get(left);
					p.factory.setLeft(l);
				}
				String right = p.annotation.right();
				if (right.length() > 0) {
					TokenFactory r = this.symbols.get(right);
					p.factory.setRight(r);
				}
				p.factory.setAllowEmpty(p.annotation.empty());
				p.factory.setAllowNone(p.annotation.none());
				p.factory.setAddError(p.annotation.adderror());
			}	
		}
		
		public void update(Class<?> cls)  throws IllegalAccessException, InstantiationException {
			this.updateChoices();
			this.updateChoicesOnChar0th();
			this.updateChoicesOnChar1st();
			this.updateSequences();
			this.updateLists();
			this.updateDelimitedLists();
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
		
	public <T> T parse(Class<T> cls) throws ParseException {
		try {
			T target = cls.newInstance();
			Store store = new Store();
			store.add(target);
			store.updateEquivalents(target);
			store.update(cls);
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
}
