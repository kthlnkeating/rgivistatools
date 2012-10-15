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

public class TString extends StringPieceImpl implements StringToken {
	private static final long serialVersionUID = 1L;
	
	public TString() {
		super();
	}

	public TString(StringPieceImpl value) {
		super(value);
	}
	
	public TString(Token token) {
		super(token.toValue());
	}
	
	public TString(String data, int beginIndex, int endIndex) {
		super(data, beginIndex, endIndex);
	}
	
	@Override
	public StringPieceImpl toValue() {
		return this;
	}
	
	public void setValue(StringPieceImpl value) {
		super.set(value);
	}

	@Override
	public void beautify() {		
	}
}
