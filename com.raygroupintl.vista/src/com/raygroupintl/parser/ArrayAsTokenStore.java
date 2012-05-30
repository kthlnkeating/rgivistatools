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

import java.util.ArrayList;
import java.util.List;

public class ArrayAsTokenStore implements TokenStore {
	private List<Token> tokens;
	int index = 0;
	int length = 0;
	
	public ArrayAsTokenStore(int length) {
		this.length = length;
	}
	
	@Override
	public void addToken(Token token) {
		++index;
		if (token == null) {
			if (this.tokens != null) {
				this.tokens.add(token);
			}
		} else {
			if (this.tokens == null) {
				this.tokens = new ArrayList<Token>(this.length);				
				for (int i=0; i<index-1; ++i) this.tokens.add(null);
			}
			this.tokens.add(token);
		}
	}
	
	@Override
	public List<Token> toList() {
		return this.tokens;
	}
	
	@Override
	public boolean isAllNull() {
		return this.tokens == null;
	}

	@Override
	public int size() {
		return this.index;
	}

	@Override
	public boolean hasToken() {
		return this.index > 0;
	}

	public void set(int index, Token token) {
		this.tokens.set(index, token);
	}
}
