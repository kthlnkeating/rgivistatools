package com.raygroupintl.bnf.annotation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.bnf.TFChoiceBasic;
import com.raygroupintl.bnf.TFSeqStatic;
import com.raygroupintl.fnds.ITokenFactory;

public class Parser {
	private static ITokenFactory newTokenFactory(Field f) {
		Choice choice = f.getAnnotation(Choice.class);
		if (choice != null) {
			TFChoiceBasic value = new TFChoiceBasic();
			return value;
		}
		Sequence sequence = f.getAnnotation(Sequence.class);
		if (sequence != null) {
			TFSeqStatic value = new TFSeqStatic();
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
	
	private static <T> Map<String, ITokenFactory> getSymbols(T target, Class<T> cls) throws IllegalAccessException, InstantiationException {
		Class<?> loopCls = cls;
		Map<String, ITokenFactory> symbols = new HashMap<String, ITokenFactory>();
		while (! loopCls.equals(Object.class)) {
			for (Field f : loopCls.getDeclaredFields()) {
				if (ITokenFactory.class.isAssignableFrom(f.getType())) {
					ITokenFactory value = (ITokenFactory) f.get(target);
					if (value == null) {
						value = newTokenFactory(f);
						f.set(target, value);
					}
					String name = f.getName();
					symbols.put(name, value);
				}
			}
			loopCls = loopCls.getSuperclass();
		}
		return symbols;
	}
	
	public static <T> T parse(Class<T> cls) throws IllegalAccessException, InstantiationException {
		T target = cls.newInstance();
		Map<String, ITokenFactory> symbols = getSymbols(target, cls);
		Class<?> loopCls = cls;
		while (! loopCls.equals(Object.class)) {
			for (Field f : loopCls.getDeclaredFields()) {
				if (ITokenFactory.class.isAssignableFrom(f.getType())) {
					Choice choice = f.getAnnotation(Choice.class);
					if (choice != null) {
						TFChoiceBasic value = (TFChoiceBasic) f.get(target);
						ITokenFactory[] fs = getFactories(symbols, choice.value());
						value.setFactories(fs);
						continue;
					}				
					Sequence sequence = f.getAnnotation(Sequence.class);
					if (sequence != null) {
						TFSeqStatic value = (TFSeqStatic) f.get(target);
						ITokenFactory[] fs = getFactories(symbols, sequence.value());
						value.setFactories(fs);
						boolean[] required = getRequiredFlags(sequence.required(), fs.length);
						value.setRequiredFlags(required);
						continue;
					}
				}
			}			
			loopCls = loopCls.getSuperclass();
		}
		return target;
	}
}
