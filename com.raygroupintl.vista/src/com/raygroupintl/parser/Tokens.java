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

public interface Tokens {
	void addToken(Token token);
	void setToken(int index, Token token);
	
	Token getToken(int index);
	Token getToken(int index0, int index1);	
	Token getToken(int index0, int index1, int index2);	
	
	Tokens getTokens(int index);
	Tokens getTokens(int index0, int index1);

	boolean hasToken();
		
	TextPiece toValue();
	
	Iterable<Token> toLogicalIterable();
	Iterable<Token> toIterable();
	
	int size();
}
