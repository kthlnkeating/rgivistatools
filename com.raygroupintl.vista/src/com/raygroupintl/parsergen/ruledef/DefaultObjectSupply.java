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
import com.raygroupintl.parser.StringToken;
import com.raygroupintl.parser.TDelimitedList;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenStore;
import com.raygroupintl.parsergen.ObjectSupply;

public class DefaultObjectSupply implements ObjectSupply {
	@Override
	public StringToken newString() {
		return new TString();
	}
	
	@Override
	public CompositeToken newSequence(int length) {
		return new TSequence(length);
	}
	
	@Override
	public CompositeToken newSequence(TokenStore store) {
		return new TSequence(store.toList());
	}
	
	@Override
	public CompositeToken newList() {
		return new TList();
	}
	
	@Override
	public CompositeToken newDelimitedList(List<Token> tokens) {
		return new TDelimitedList(tokens);
	}
	
	@Override
	public TEmpty newEmpty() {
		return new TEmpty();
	}
}