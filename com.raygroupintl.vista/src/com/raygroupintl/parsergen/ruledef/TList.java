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

import java.util.List;

import com.raygroupintl.parser.CompositeToken;
import com.raygroupintl.parser.ListStore;
import com.raygroupintl.parser.Token;

class TList extends ListStore implements CompositeToken {
	public TList() {
	}
	
	public TList(List<Token> tokens) {
		super(tokens);
	}

	@Override
	public void beautify() {
		for (Token token : this) {
			token.beautify();
		}
	}
}
