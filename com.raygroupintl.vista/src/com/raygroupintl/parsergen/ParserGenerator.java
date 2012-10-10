//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.parsergen;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.annotation.ParseErrorException;
import com.raygroupintl.parser.annotation.ParseException;

public abstract class ParserGenerator {
	protected static abstract class TokenFactoryStore {		
		protected abstract TokenFactory add(Field f);
		
		protected abstract <T> boolean handleField(T target, Field f) throws IllegalAccessException;

		private <T> void handleWithRemaining(T target, Field f, Set<String> remainingNames, java.util.List<Field> remaining) throws IllegalAccessException{
			String name = f.getName();
			if (remainingNames.contains(name)) {
				remaining.add(f);							
				return;
			}
			if (! this.handleField(target, f)) {
				remainingNames.add(name);
				remaining.add(f);
			}			
		}
		
		public <T> void add(T target) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
			Set<String> remainingNames = new HashSet<String>();
			java.util.List<Field> remaining = new ArrayList<Field>();
			Class<?> cls = target.getClass();
			while (! cls.equals(Object.class)) {
				for (Field f : cls.getDeclaredFields()) {
					if (TokenFactory.class.isAssignableFrom(f.getType())) {
						this.handleWithRemaining(target, f, remainingNames, remaining);
					}
				}
				cls = cls.getSuperclass();
			}
			while (remaining.size() > 0) {
				remainingNames = new HashSet<String>();
				java.util.List<Field> loopRemaining = new ArrayList<Field>();
				for (Field f : remaining) {
					this.handleWithRemaining(target, f, remainingNames, loopRemaining);
				}
				if (remaining.size() == loopRemaining.size()) {
					String symbols = "";
					for (Field f : remaining) {
						symbols += ", " + f.getName();
					}
					throw new ParseErrorException("Following symbols are not resolved: " + symbols.substring(1));
				}
				remaining = loopRemaining;
			}			
		}
		
		public abstract void addAssumed();

		public abstract void update(Class<?> cls) throws IllegalAccessException, InstantiationException, ParseException;
	}
	
	protected abstract TokenFactoryStore getStore();
	
	public <T> T generate(Class<T> cls) throws ParseException {
		try {
			T target = cls.newInstance();
			TokenFactoryStore store = this.getStore();
			store.add(target);
			store.addAssumed();
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
