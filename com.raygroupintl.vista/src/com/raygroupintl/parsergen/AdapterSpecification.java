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

public class AdapterSpecification {
	private Class<? extends Token> token;
	private Class<? extends Token> string;
	private Class<? extends Token> list;
	private Class<? extends Token> delimitedList;
	private Class<? extends Token> sequence;

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
	
	public void addCopy(Field f) {
		TokenType tokenType = f.getAnnotation(TokenType.class);
		if (tokenType != null) {
			this.token = tokenType.value();
			++this.count;
		}		
	}
	
	public void addString(Field f) {
		StringTokenType stringTokenType = f.getAnnotation(StringTokenType.class);
		if (stringTokenType != null) {
			this.string = stringTokenType.value();
			++this.count;
		}
	}
	
	public void addList(Field f) {
		ListTokenType listTokenType = f.getAnnotation(ListTokenType.class);
		if (listTokenType != null) {
			this.list = listTokenType.value();
			++this.count;
		}
	}
	
	public void addSequence(Field f) {
		SequenceTokenType seqTokenType = f.getAnnotation(SequenceTokenType.class);
		if (seqTokenType != null) {
			this.sequence = seqTokenType.value();
			++this.count;
		}
	}
	
	public void addDelimitedList(Field f) {
		DelimitedListTokenType dlTokenType = f.getAnnotation(DelimitedListTokenType.class);
		if (dlTokenType != null) {
			this.delimitedList = dlTokenType.value();
			++this.count;
		}		
	}
	
	public static AdapterSpecification getInstance(Field f) {
		AdapterSpecification result = new AdapterSpecification();
		result.addCopy(f);
		result.addString(f);
		result.addList(f);
		result.addSequence(f);
		result.addDelimitedList(f);
		
		if (result.count > 1) {
			throw new ParseErrorException("Multiple adapters are not allowed.");								
		}
		
		return result;
	}
}
