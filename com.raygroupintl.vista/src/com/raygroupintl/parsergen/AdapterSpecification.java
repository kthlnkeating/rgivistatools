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

import com.raygroupintl.parser.Token;

public class AdapterSpecification<T extends Token> {
	private Class<? extends T> token;
	private Class<? extends T> string;
	private Class<? extends T> list;
	private Class<? extends T> delimitedList;
	private Class<? extends T> sequence;

	private int count;
	
	public <M> M getNull() {
		if (this.count > 0) {
			throw new ParseErrorException("Uncompatible adapter type.");
		}
		return null;
	}
	
	public Class<? extends Token> getTokenAdapter() {
		if (this.token != null) {
			return this.token;
		}
		return getNull();
	}
	
	public Class<? extends Token> getStringTokenAdapter() {
		if (this.string != null) {
			return this.string;
		}
		return getNull();
	}
	
	public Class<? extends Token> getListTokenAdapter() {
		if (this.list != null) {
			return this.list;
		}
		return getNull();
	}
	
	public Class<? extends Token> getDelimitedListTokenAdapter() {
		if (this.delimitedList != null) {
			return this.delimitedList;
		}
		return getNull();
	}
	
	public Class<? extends Token> getSequenceTokenAdapter() {
		if (this.sequence != null) {
			return this.sequence;
		}
		return getNull();
	}
	
	private Class<? extends T> addGeneric(Class<? extends Token> raw, Class<T> actualTokencls) {
		if (actualTokencls.isAssignableFrom(raw)) {
			@SuppressWarnings("unchecked")
			Class<? extends T> local = (Class<? extends T>) raw;
			++this.count;
			return local;
		}
		throw new ParseErrorException(raw.getName() + " is not a sub class of " + actualTokencls.getName() + ".");
	}
	
	public void addCopy(Field f, Class<T> actualTokencls) {
		TokenType tokenType = f.getAnnotation(TokenType.class);
		if (tokenType != null) {
			this.token = this.addGeneric(tokenType.value(), actualTokencls);
		}		
	}
	
	public void addString(Field f, Class<T> actualTokencls) {
		StringTokenType stringTokenType = f.getAnnotation(StringTokenType.class);
		if (stringTokenType != null) {
			this.string = this.addGeneric(stringTokenType.value(), actualTokencls);
		}
	}
	
	public void addList(Field f, Class<T> actualTokencls) {
		ListTokenType listTokenType = f.getAnnotation(ListTokenType.class);
		if (listTokenType != null) {
			this.list = this.addGeneric(listTokenType.value(), actualTokencls);
		}
	}
	
	public void addSequence(Field f, Class<T> actualTokencls) {
		SequenceTokenType seqTokenType = f.getAnnotation(SequenceTokenType.class);
		if (seqTokenType != null) {
			this.sequence = this.addGeneric(seqTokenType.value(), actualTokencls);
		}
	}
	
	public void addDelimitedList(Field f, Class<T> actualTokencls) {
		DelimitedListTokenType dlTokenType = f.getAnnotation(DelimitedListTokenType.class);
		if (dlTokenType != null) {
			this.delimitedList = this.addGeneric(dlTokenType.value(), actualTokencls);
		}		
	}
	
	public static <T extends Token> AdapterSpecification<T> getInstance(Field f, Class<T> tokenCls) {
		AdapterSpecification<T> result = new AdapterSpecification<T>();
		result.addCopy(f, tokenCls);
		result.addString(f, tokenCls);
		result.addList(f, tokenCls);
		result.addSequence(f, tokenCls);
		result.addDelimitedList(f, tokenCls);
		
		if (result.count > 1) {
			throw new ParseErrorException("Multiple adapters are not allowed.");								
		}
		
		return result;
	}
}
