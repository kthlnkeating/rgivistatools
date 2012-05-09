package com.raygroupintl.bnf.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.bnf.TFCharacters;
import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFChoiceOnChar0th;
import com.raygroupintl.bnf.TFChoiceOnChar1st;
import com.raygroupintl.bnf.TFDelimitedList;
import com.raygroupintl.bnf.TFSequenceStatic;
import com.raygroupintl.bnf.TokenAdapter;
import com.raygroupintl.fnds.ICharPredicate;
import com.raygroupintl.m.struct.IdentifierStartPredicate;
import com.raygroupintl.struct.AndPredicate;
import com.raygroupintl.struct.CharPredicate;
import com.raygroupintl.struct.CharRangePredicate;
import com.raygroupintl.struct.CharsPredicate;
import com.raygroupintl.struct.DigitPredicate;
import com.raygroupintl.struct.ExcludePredicate;
import com.raygroupintl.struct.LetterPredicate;
import com.raygroupintl.struct.OrPredicate;

public class Parser {
	private static final Map<String, ICharPredicate> PREDICATES = new HashMap<String, ICharPredicate>();
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
	
	private static class Store {
		public Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
		
		private java.util.List<Triple<TFChoiceBasic, Choice>> choices  = new ArrayList<Triple<TFChoiceBasic, Choice>>();
		private java.util.List<Triple<TFSequenceStatic, Sequence>> sequences  = new ArrayList<Triple<TFSequenceStatic, Sequence>>();
		private java.util.List<Triple<TFSequenceStatic, Description>> descriptions  = new ArrayList<Triple<TFSequenceStatic, Description>>();
		private java.util.List<Triple<TFDelimitedList, List>> lists  = new ArrayList<Triple<TFDelimitedList, List>>();
		private java.util.List<Triple<TFChoiceOnChar0th, CChoice>> choice0ths  = new ArrayList<Triple<TFChoiceOnChar0th, CChoice>>();
		private java.util.List<Triple<TFChoiceOnChar1st, CChoice>> choice1sts  = new ArrayList<Triple<TFChoiceOnChar1st, CChoice>>();
		private Map<String, Field> otherSymbols = new HashMap<String, Field>();
		
		private TokenFactory addChoice(String name, Choice choice) {
			TFChoiceBasic value = new TFChoiceBasic();
			this.choices.add(new Triple<TFChoiceBasic, Choice>(name, value, choice));
			return value;			
		}
		
		private TokenFactory addSequence(String name, Sequence sequence, Adapter adapter)  throws IllegalAccessException, InstantiationException {
			TFSequenceStatic value = new TFSequenceStatic();
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				TokenAdapter ta = (TokenAdapter) cls.newInstance();
				value.setTokenAdapter(ta);
			}			
			this.sequences.add(new Triple<TFSequenceStatic, Sequence>(name, value, sequence));
			return value;			
		}
		
		private TokenFactory addDescription(String name, Description description, Adapter adapter)  throws IllegalAccessException, InstantiationException {
			TFSequenceStatic value = new TFSequenceStatic();
			if (adapter != null) {
				Class<?> cls = adapter.value();			
				TokenAdapter ta = (TokenAdapter) cls.newInstance();
				value.setTokenAdapter(ta);
			}			
			this.descriptions.add(new Triple<TFSequenceStatic, Description>(name, value, description));
			return value;		
		}
		
		private TokenFactory addList(String name, List list) {
			TFDelimitedList value = new TFDelimitedList();
			this.lists.add(new Triple<TFDelimitedList, List>(name, value, list));
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
		
		private TokenFactory addCharacters(String name, Characters characters) {
			ICharPredicate p0 = getCharPredicate(characters.chars());
			ICharPredicate p1 = getCharRanges(characters.ranges());
			ICharPredicate p2 = getCharPredicate(characters.excludechars());
			if (p2 != null) {
				p2 = new ExcludePredicate(p2);
			}			
			ICharPredicate p3 = getCharRanges(characters.excluderanges());
			if (p3 != null) {
				p3 = new ExcludePredicate(p3);
			}			
			ICharPredicate[] ps = {p0, p1, p2, p3};
			ICharPredicate result = null;
			for (ICharPredicate p : ps) {
				if (p != null) {
					if (result == null) {
						result = p;
					} else {
						result = new AndPredicate(result, p);
					}
				}
			}
			TokenFactory tf = new TFCharacters(result);
			return tf;						
		}
		
		private TokenFactory add(Field f) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
			String name = f.getName();
			
			Adapter adapter = f.getAnnotation(Adapter.class);
			
			Choice choice = f.getAnnotation(Choice.class);
			if (choice != null) {
				return this.addChoice(name, choice);
			}			
			Sequence sequence = f.getAnnotation(Sequence.class);
			if (sequence != null) {
				return this.addSequence(name, sequence, adapter);
			}			
			Description description = f.getAnnotation(Description.class);
			if (description != null) {
				return this.addDescription(name, description, adapter);
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
				return this.addCharacters(name, characters);
			}
			return null;
		}
		
		public <T> void add(T target) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
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
				ICharPredicate[] ps = getPredicates(p.annotation.preds());
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
				ICharPredicate[] ps = getPredicates(p.annotation.preds());
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
	
		private void updateLists() {
			for (Triple<TFDelimitedList, List> p : this.lists) {
				TokenFactory f = this.symbols.get(p.annotation.value());
				p.factory.setElementFactory(f);
				String delim = p.annotation.delim();
				if ((delim != null) && (delim.length() > 0)) {
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
		}		
	}
	
	private static ICharPredicate getCharPredicate(char[] chs) {
		if (chs.length == 1) {
			return new CharPredicate(chs[0]);
		} else if (chs.length > 1) {
			return new CharsPredicate(chs);
		} else {
			return null;
		}		
	}
	
	private static ICharPredicate getCharRanges(char[] chs) {
		ICharPredicate result = null;
		for (int i=1; i<chs.length; i=i+2) {
			char ch0 = chs[i];
			char ch1 = chs[i-1];
			ICharPredicate p =  new CharRangePredicate(ch0, ch1);
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
	
	private static ICharPredicate[] getPredicates(String[] codes) {
		int n = codes.length;
		ICharPredicate[] result = new ICharPredicate[n];
		for (int i=0; i<n; ++i) {
			String code = codes[i];
			if (code.length() == 1) {
				result[i] = new CharPredicate(code.charAt(0));
			} else {
				ICharPredicate named = PREDICATES.get(code);
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
		}
	}
}
