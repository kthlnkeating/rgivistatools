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

package com.raygroupintl.m.token;

import com.raygroupintl.parser.EmptyToken;
import com.raygroupintl.parser.ListOfTokens;
import com.raygroupintl.parser.SequenceOfTokens;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ObjectSupply;

public class MObjectSupply implements ObjectSupply {
	@Override
	public MString newString() {
		return new MString();
	}
	
	@Override
	public MSequence newSequence(SequenceOfTokens tokens) {
		return new MSequence(tokens);
	}
	
	@Override
	public MList newList(ListOfTokens tokens) {
		return new MList(tokens);
	}
	
	@Override
	public MDelimitedList newDelimitedList(Token leadingToken, Tokens tailTokens) {
		return new MDelimitedList(leadingToken, tailTokens);
	}
	
	@Override
	public EmptyToken newEmpty() {
		return new MEmpty();
	}
}
