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

public abstract class TFBasic extends TokenFactory {
	private Adapter adapter;

	public TFBasic(String name) {
		super(name);
	}

	
	@Override
	protected Token convert(Token token) {
		if ((this.adapter != null) && (token != null)) {
			return this.adapter.convert(token); 
		} else {
			return token;
		}
	}

	public void setTargetType(Class<? extends Token> cls) {
		final Constructor<? extends Token> constructor = getConstructor(cls, Token.class, Token.class);
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
	
	static protected Constructor<? extends Token> getConstructor(Class<? extends Token> cls, Class<?> constructorArgument, Class<? extends Token> targetCls) {
		try {
			if (! targetCls.isAssignableFrom(cls)) {
				throw new IllegalArgumentException(cls.getName() + " must extend " + targetCls.getName() + ".");
			}
			int modifiers = cls.getModifiers();
			if (! Modifier.isPublic(modifiers)) {
				throw new IllegalArgumentException(cls.getName() + " is not public.");
			}
			if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
				throw new IllegalArgumentException(cls.getName() + " is abstract.");
			}
			final Constructor<? extends Token> constructor = cls.getConstructor(constructorArgument);
			if (! Modifier.isPublic(constructor.getModifiers())) {
				throw new IllegalArgumentException(cls.getName() + " constructor (List) is not public.");			
			}
			return constructor;
		} catch (NoSuchMethodException nsm) {
			throw new IllegalArgumentException(cls.getName() + " does not have a constructor that accepts " + constructorArgument.getName() + ".");
		}
	}
}
