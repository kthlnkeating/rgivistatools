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

public class TokensVisitor {
	private Tokens result;
	
	public void visitSingle() {
		this.result = null;
	}
	
	public void visitMultiple(Tokens store) {
		this.result = store;
	}
	
	public Tokens toTokenStore(Token token) {
		token.accept(this);
		return this.result;
	}	
}
