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

import com.raygroupintl.parsergen.ObjectSupply;

public abstract class TokenFactorySupply<T extends Token> extends TokenFactory<T> {
	public TokenFactorySupply(String name) {
		super(name);
	}
	
	public abstract TokenFactory<T> getSupplyTokenFactory();
	
	public abstract TokenFactory<T> getNextTokenFactory(T token) throws SyntaxErrorException;
	
	public abstract T getToken(T supplyToken, T nextToken);
	
	@Override
	public T tokenize(Text text, ObjectSupply<T> objectSupply) throws SyntaxErrorException {
		TokenFactory<T> supplyFactory = getSupplyTokenFactory();
		T token = supplyFactory.tokenize(text, objectSupply);
		if (token == null) {
			return null;
		} else {
			TokenFactory<T> tf = this.getNextTokenFactory(token);
			if (tf == null) {
				return token;
			} else {
				T nextToken = tf.tokenize(text, objectSupply);
				return this.getToken(token, nextToken);
			}
		}
	}	
}
