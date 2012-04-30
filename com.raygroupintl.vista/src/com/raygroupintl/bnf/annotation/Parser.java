package com.raygroupintl.bnf.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFDelimitedList;

public class Parser {
	private static class Pair<T extends ITokenFactory, A extends Annotation> {
		public T factory;
		public A annotation;
		
		public Pair(T factory, A annotation) {
			this.factory = factory;
			this.annotation = annotation;
		}
	}
	
	private static class Store {
		Map<String, ITokenFactory> symbols = new HashMap<String, ITokenFactory>();
		java.util.List<Pair<TFChoiceBasic, Choice>> choices  = new ArrayList<Pair<TFChoiceBasic, Choice>>();
		java.util.List<Pair<TFSeqStatic, Sequence>> sequences  = new ArrayList<Pair<TFSeqStatic, Sequence>>();
		java.util.List<Pair<TFDelimitedList, List>> lists  = new ArrayList<Pair<TFDelimitedList, List>>();
	}
	
	private static ITokenFactory newTokenFactory(Field f, Store store) {
		Choice choice = f.getAnnotation(Choice.class);
		if (choice != null) {
			TFChoiceBasic value = new TFChoiceBasic();
			store.choices.add(new Pair<TFChoiceBasic, Choice>(value, choice));
			return value;
		}
		Sequence sequence = f.getAnnotation(Sequence.class);
		if (sequence != null) {
			TFSeqStatic value = new TFSeqStatic();
			store.sequences.add(new Pair<TFSeqStatic, Sequence>(value, sequence));
			return value;
		}
		List list = f.getAnnotation(List.class);
		if (list != null) {
			TFDelimitedList value = new TFDelimitedList();
			store.lists.add(new Pair<TFDelimitedList, List>(value, list));
			return value;
		}
		return null;
	}
	
	private static ITokenFactory[] getFactories(Map<String, ITokenFactory> map, String[] names) {
		int n = names.length;
		ITokenFactory[] fs = new ITokenFactory[n];
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
	
	private static <T> Store getStore(T target, Class<T> cls) throws IllegalAccessException, InstantiationException {
		Class<?> loopCls = cls;
		Store store = new Store();
		while (! loopCls.equals(Object.class)) {
			for (Field f : loopCls.getDeclaredFields()) {
				if (ITokenFactory.class.isAssignableFrom(f.getType())) {
					ITokenFactory value = (ITokenFactory) f.get(target);
					if (value == null) {
						value = newTokenFactory(f, store);
						f.set(target, value);
					}
					String name = f.getName();
					store.symbols.put(name, value);
				}
			}
			loopCls = loopCls.getSuperclass();
		}
		return store;
	}
	
	public static <T> T parse(Class<T> cls) throws IllegalAccessException, InstantiationException {
		T target = cls.newInstance();
		Store store = getStore(target, cls);
		for (Pair<TFChoiceBasic, Choice> p : store.choices) {
			ITokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			p.factory.setFactories(fs);
		}
		for (Pair<TFSeqStatic, Sequence> p : store.sequences) {
			ITokenFactory[] fs = getFactories(store.symbols, p.annotation.value());
			p.factory.setFactories(fs);
			boolean[] required = getRequiredFlags(p.annotation.required(), fs.length);
			p.factory.setRequiredFlags(required);
		}
		for (Pair<TFDelimitedList, List> p : store.lists) {
			ITokenFactory f = store.symbols.get(p.annotation.value());
			p.factory.setElementFactory(f);
			String delim = p.annotation.delim();
			if (delim.length() > 0) {
				ITokenFactory d = store.symbols.get(delim);
				p.factory.setDelimiter(d);
			}
			String left = p.annotation.left();
			if (left.length() > 0) {
				ITokenFactory l = store.symbols.get(left);
				p.factory.setLeft(l);
			}
			String right = p.annotation.right();
			if (right.length() > 0) {
				ITokenFactory r = store.symbols.get(right);
				p.factory.setRight(r);
			}
			p.factory.setAllowEmpty(p.annotation.empty());
		}
		return target;
	}
}
