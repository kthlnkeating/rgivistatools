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

public class ListAsTokenStore implements TokenStore {
	private List<Token> list;
	
	@Override
	public void addToken(Token token) {
		if (this.list == null) {
			this.list = new ArrayList<Token>();
		}
		this.list.add(token);
	}

	@Override
	public List<Token> toList() {
		return this.list;
	}
	
	@Override
	public boolean isAllNull() {
		return (this.list == null) || (this.list.size() == 0);
	}

	//@Override
	//public Token get(int index) {
	//	return this.list.get(index);
	//}
	
	@Override
	public int size() {
		return this.list == null ? 0 : this.list.size();
	}

	@Override
	public boolean hasToken() {
		return this.list != null;
	}
	
	public void set(int index, Token token) {
		this.list.set(index, token);
	}
}
