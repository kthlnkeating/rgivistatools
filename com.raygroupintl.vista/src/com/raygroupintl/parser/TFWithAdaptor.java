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

import com.raygroupintl.parsergen.ObjectSupply;

public abstract class TFWithAdaptor<T extends Token> extends TokenFactory<T> {	
	private Constructor<? extends T> ctr;

	public TFWithAdaptor(String name) {
		super(name);
	}
	
	@Override
	public T tokenize(Text text, ObjectSupply<T> objectSupply) throws SyntaxErrorException {
		T result = this.tokenizeOnly(text, objectSupply);
		return this.convert(result);
	}
	
	protected abstract T tokenizeOnly(Text text, ObjectSupply<T> objectSupply) throws SyntaxErrorException;
	
	protected final T convert(T token) {
		if ((this.ctr != null) && (token != null)) {
			try{
				return this.ctr.newInstance(token); 
			} catch (Throwable t) {
				throw new IllegalStateException("Invalid token type had been assigned.");
			}
		} else {
			return token;
		}
	}

	public <M extends Token> void setTargetType(Constructor<? extends T> constructor) {
		this.ctr = constructor;
	}
	
	@Override
	public T convertToken(T token) {
		return this.convert(token);
	}
}
