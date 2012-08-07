package com.raygroupintl.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIParamMgr {
	private static void updateValues(Object target, Field positionalField, Map<String, Field> namedFields, String[] args) throws IllegalAccessException {
		int index = 0;
		while (index < args.length) {
			String arg = args[index];
			if (namedFields.containsKey(arg)) {
				Field f = namedFields.get(arg);
				Class<?> cls = f.getType();
				if (cls.equals(boolean.class)) {
					f.setBoolean(target, true);
				} else {
					++index;
					arg = args[index];
					if (List.class.isAssignableFrom(cls)) {	
						@SuppressWarnings("unchecked")
						List<String> v = (List<String>) f.get(target);
						v.add(arg);
					} else {
						f.set(target, arg);
					}
				}
			} else if (positionalField != null) {
				@SuppressWarnings("unchecked")
				List<String> v = (List<String>) positionalField.get(target);
				v.add(arg);
			}
			++index;
		}
	}
	
	public static <T> void update(T target, Class<T> cls, String[] args) throws IllegalAccessException {
		Map<String, Field> namedFields = new HashMap<String, Field>();
		Field positionalField = null;
		for (Field f : cls.getDeclaredFields()) {			
			CLIParameter annotation = f.getAnnotation(CLIParameter.class);
			if (annotation != null) {
				String[] names = annotation.names();				
				if (names.length == 0) {
					positionalField = f;
				} else for (String name : annotation.names()) {
					namedFields.put(name, f);
				}
			}
		}
		updateValues(target, positionalField, namedFields, args);		
	}
	
	public static <T> T parse(Class<T> cls, String[] args) throws IllegalAccessException, InstantiationException {
		T target = cls.newInstance();
		update(target, cls, args);
		return target;
	}
}