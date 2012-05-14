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

package com.raygroupintl.bnf;

public class TFConstant extends TokenFactory {
	private String value;
	private boolean ignoreCase;
	private StringAdapter adapter;
	
	public TFConstant(String value, StringAdapter adapter) {
		this.value = value;
		this.adapter = adapter;
		this.ignoreCase = false;
	}
	
	public TFConstant(String value, StringAdapter adapter, boolean ignoreCase) {
		this.value = value;
		this.adapter = adapter;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public Token tokenize(Text text) {
		return text.extractToken(this.value, this.adapter, this.ignoreCase);
	}
}
