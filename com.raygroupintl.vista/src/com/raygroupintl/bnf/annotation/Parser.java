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
		Map<String, TokenFactory> symbols = new HashMap<String, TokenFactory>();
		java.util.List<Triple<TFChoiceBasic, Choice>> choices  = new ArrayList<Triple<TFChoiceBasic, Choice>>();
		java.util.List<Triple<TFSequenceStatic, Sequence>> sequences  = new ArrayList<Triple<TFSequenceStatic, Sequence>>();
		java.util.List<Triple<TFDelimitedList, List>> lists  = new ArrayList<Triple<TFDelimitedList, List>>();
		java.util.List<Triple<TFChoiceOnChar0th, CChoice>> choice0ths  = new ArrayList<Triple<TFChoiceOnChar0th, CChoice>>();
		java.util.List<Triple<TFChoiceOnChar1st, CChoice>> choice1sts  = new ArrayList<Triple<TFChoiceOnChar1st, CChoice>>();
	}
	
	private static TokenFactory newTokenFactory(Field f, Store store) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Choice choice = f.getAnnotation(Choice.class);
		String name = f.getName();
		if (choice != null) {
			TFChoiceBasic value = new TFChoiceBasic();
			store.choices.add(new Triple<TFChoiceBasic, Choice>(name, value, choice));
			return value;
		}
		Sequence sequence = f.getAnnotation(Sequence.class);
		if (sequence != null) {
			TFSequenceStatic value = new TFSequenceStatic();
			store.sequences.add(new Triple<TFSequenceStatic, Sequence>(name, value, sequence));
			return value;
		}
		List list = f.getAnnotation(List.class);
		if (list != null) {
			TFDelimitedList value = new TFDelimitedList();
			store.lists.add(new Triple<TFDelimitedList, List>(name, value, list));
			return value;
		}
		CChoice cchoice = f.getAnnotation(CChoice.class);
		if (cchoice != null) {
			String lead = cchoice.lead();
			if (lead.length() == 0) {
				TFChoiceOnChar0th value = new TFChoiceOnChar0th();
				store.choice0ths.add(new Triple<TFChoiceOnChar0th, CChoice>(name, value, cchoice));
				return value;
			} else {
				TFChoiceOnChar1st value = new TFChoiceOnChar1st();
				store.choice1sts.add(new Triple<TFChoiceOnChar1st, CChoice>(name, value, cchoice));
				return value;
			}
		}
		Characters characters = f.getAnnotation(Characters.class);
		if (characters != null) {
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
		return null;
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
	
	private static <T> void assignEquivalents(T target, Map<String, Field> lazySymbols, Store store) throws IllegalAccessException {
		for (String name : lazySymbols.keySet()) {			
			Field f = lazySymbols.get(name);
			Equivalent annot = f.getAnnotation(Equivalent.class);
			if (annot != null) {
				String source = annot.value();
				TokenFactory sourceFactory = store.symbols.get(source);
				f.set(target, sourceFactory);
				store.symbols.put(name, sourceFactory);
			}
			
		}
	}
	
	private static <T> Store getStore(T target, Class<T> cls) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		Class<?> loopCls = cls;
		Map<String, Field> lazySymbols = new HashMap<String, Field>();
		Store store = new Store();
		while (! loopCls.equals(Object.class)) {
			for (Field f : loopCls.getDeclaredFields()) {
				if (TokenFactory.class.isAssignableFrom(f.getType())) {
					String name = f.getName();
					TokenFactory already = store.symbols.get(name);
					if (already == null) {					
						TokenFactory value = (TokenFactory) f.get(target);
						if (value == null) {
							value = newTokenFactory(f, store);
							if (value != null) {
								f.set(target, value);
							} else {
								lazySymbols.put(name, f);
							}
						}
						if (value != null) {
							store.symbols.put(name, value);
						}
					} else {
						f.set(target, already);						
					}
				}
			}
			loopCls = loopCls.getSuperclass();
		}
		assignEquivalents(target, lazySymbols, store);
		return store;
	}
	
	private static <T> Map<String, TokenAdapter> getAdapters(Class<T> cls) throws IllegalAccessException, InstantiationException{
		Map<String, TokenAdapter> result = new HashMap<String, TokenAdapter>();
		Class<?> loopCls = cls;
		while (! loopCls.equals(Object.class)) {
			for (Class<?> c : loopCls.getDeclaredClasses()) {
				Adapter adapter = c.getAnnotation(Adapter.class);
				if (adapter != null) {			
					String n = adapter.value();
					if (! result.containsKey(n)) {
						TokenAdapter ta = (TokenAdapter) c.newInstance();
						result.put(n, ta);
					}
				}
			}
			loopCls = loopCls.getSuperclass();
		}
		return result;
	}
	
	public static <T> T parse(Class<T> cls) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		T target = cls.newInstance();
		Store store = getStore(target, cls);
		Map<String, TokenAdapter> tas = getAdapters(cls);
		for (Triple<TFChoiceBasic, Choice> p : store.choices) {
			TokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			p.factory.setFactories(fs);
		}
		for (Triple<TFChoiceOnChar0th, CChoice> p : store.choice0ths) {
			TokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			ICharPredicate[] ps = getPredicates(p.annotation.preds());
			p.factory.setChoices(ps, fs);
			String dcode = p.annotation.def();
			if (dcode.length() > 0) {
				TokenFactory df = store.symbols.get(dcode);
				p.factory.setDefault(df);
			}
		}
		for (Triple<TFChoiceOnChar1st, CChoice> p : store.choice1sts) {
			TokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			ICharPredicate[] ps = getPredicates(p.annotation.preds());
			p.factory.setLeadingChar(p.annotation.lead().charAt(0));
			p.factory.setChoices(ps, fs);
			String dcode = p.annotation.def();
			if (dcode.length() > 0) {
				TokenFactory df = store.symbols.get(dcode);
				p.factory.setDefault(df);
			}
		}
		for (Triple<TFSequenceStatic, Sequence> p : store.sequences) {
			TokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			p.factory.setFactories(fs);
			boolean[] required = getRequiredFlags(p.annotation.required(), fs.length);
			p.factory.setRequiredFlags(required);			
			TokenAdapter adapter = tas.get(p.name);
			if (adapter != null) {
				p.factory.setTokenAdapter(adapter);					
			}
		}
		for (Triple<TFDelimitedList, List> p : store.lists) {
			TokenFactory f = store.symbols.get(p.annotation.value());
			p.factory.setElementFactory(f);
			String delim = p.annotation.delim();
			if ((delim != null) && (delim.length() > 0)) {
				TokenFactory d = store.symbols.get(delim);
				p.factory.setDelimiter(d);
			}
			String left = p.annotation.left();
			if (left.length() > 0) {
				TokenFactory l = store.symbols.get(left);
				p.factory.setLeft(l);
			}
			String right = p.annotation.right();
			if (right.length() > 0) {
				TokenFactory r = store.symbols.get(right);
				p.factory.setRight(r);
			}
			p.factory.setAllowEmpty(p.annotation.empty());
			p.factory.setAllowNone(p.annotation.none());
			p.factory.setAddError(p.annotation.adderror());
		}
		return target;
	}
}
