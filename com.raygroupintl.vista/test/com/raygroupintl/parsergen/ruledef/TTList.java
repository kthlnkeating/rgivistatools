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

package com.raygroupintl.parsergen.ruledef;

import com.raygroupintl.parser.ListOfTokens;
import com.raygroupintl.parser.Token;

public class TTList extends ListOfTokens<Token> implements Token {
	public TTList() {
	}
	
	public TTList(ListOfTokens<Token> tokens) {
		super(tokens);
	}

	@Override
	public void beautify() {
		for (Token token : this.toIterable()) {
			token.beautify();
		}
	}
}