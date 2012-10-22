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
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ObjectSupply;

public class DefaultObjectSupply implements ObjectSupply<RuleSupply> {
	@Override
	public TString newString(TextPiece piece) {
		return new TString(piece);
	}
	
	@Override
	public RuleSupply newSequence(SequenceOfTokens<RuleSupply> tokens) {
		return new TSequence(tokens);
	}
	
	@Override
	public RuleSupply newList(ListOfTokens<RuleSupply> tokens) {
		return new TList(tokens);
	}
	
	@Override
	public RuleSupply newDelimitedList(RuleSupply leadingToken, Tokens<RuleSupply> tailTokens) {
		return new TDelimitedList(leadingToken, tailTokens);
	}
	
	@Override
	public TEmpty newEmpty() {
		return new TEmpty();
	}
}