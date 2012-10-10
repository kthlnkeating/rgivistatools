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

public abstract class TokenFactorySupply extends TokenFactory {
	public TokenFactorySupply(String name) {
		super(name);
	}
	
	public abstract TokenFactory getSupplyTokenFactory();
	
	public abstract TokenFactory getNextTokenFactory(Token token) throws SyntaxErrorException;
	
	public abstract Token getToken(Token supplyToken, Token nextToken);
	
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		TokenFactory supplyFactory = getSupplyTokenFactory();
		Token token = supplyFactory.tokenize(text, objectSupply);
		if (token == null) {
			return null;
		} else {
			TokenFactory tf = this.getNextTokenFactory(token);
			if (tf == null) {
				return token;
			} else {
				Token nextToken = tf.tokenize(text, objectSupply);
				return this.getToken(token, nextToken);
			}
		}
	}	
}
