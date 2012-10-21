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

	private boolean hasAdapter;
	
	public <M> M getNull() {
		if (this.hasAdapter) {
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
	
	public static AdapterSpecification getInstance(Field f) {
		AdapterSpecification result = new AdapterSpecification();
		int count = 0;
		
		TokenType tokenType = f.getAnnotation(TokenType.class);
		if (tokenType != null) {
			result.token = tokenType.value();
			++count;
		}
		StringTokenType stringTokenType = f.getAnnotation(StringTokenType.class);
		if (stringTokenType != null) {
			result.string = stringTokenType.value();
			++count;
		}
		ListTokenType listTokenType = f.getAnnotation(ListTokenType.class);
		if (listTokenType != null) {
			result.list = listTokenType.value();
			++count;
		}
		SequenceTokenType seqTokenType = f.getAnnotation(SequenceTokenType.class);
		if (seqTokenType != null) {
			result.sequence = seqTokenType.value();
			++count;
		}
		DelimitedListTokenType dlTokenType = f.getAnnotation(DelimitedListTokenType.class);
		if (dlTokenType != null) {
			result.delimitedList = dlTokenType.value();
			++count;
		}
		
		if (count > 1) {
			throw new ParseErrorException("Multiple adapters are not allowed.");								
		}
		result.hasAdapter = (count > 0);
		
		return result;
	}
}
