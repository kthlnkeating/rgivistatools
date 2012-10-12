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

package com.raygroupintl.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import com.raygroupintl.parsergen.ObjectSupply;

public abstract class TokenFactory {
	private String name;
	private Adapter adapter;
	
	protected TokenFactory(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getSequenceCount() {
		return 1;
	}
	
	public final Token tokenize(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		Token result = this.tokenizeOnly(text, objectSupply);
		return this.convert(result);
	}
	
	protected final Token convert(Token token) {
		if ((this.adapter != null) && (token != null)) {
			return this.adapter.convert(token); 
		} else {
			return token;
		}
	}

	protected abstract Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException;
	
	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, Token.class);
		this.adapter = new Adapter() {			
			@Override
			public Token convert(Token ch) {
				try{
					return constructor.newInstance(ch);
				} catch (Exception e) {	
					return null;
				}
			}
		};
	}
	
	protected <T> Constructor<? extends T> getConstructor(Class<? extends T> cls, Class<?> argument) {
		try {
			int modifiers = cls.getModifiers();
			if (! Modifier.isPublic(modifiers)) {
				throw new IllegalArgumentException(this.name + ": " + cls.getName() + " is not public.");
			}
			if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
				throw new IllegalArgumentException(this.name + ": " + cls.getName() + " is abstract.");
			}
			final Constructor<? extends T> constructor = cls.getConstructor(argument);
			if (! Modifier.isPublic(constructor.getModifiers())) {
				throw new IllegalArgumentException(this.name + ": " + cls.getName() + " constructor (" + argument.getName() + ") is not public.");			
			}
			return constructor;
		} catch (NoSuchMethodException nsm) {
			throw new IllegalArgumentException(this.name + ": " + cls.getName() + " does not have a constructor that accepts " + argument.getName() + ".");
		}
	}
}