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

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.LocalReference;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.parser.StringPiece;
import com.raygroupintl.parser.Tokens;

public class MLocalByRef extends MSequence {
	public MLocalByRef(int length) {
		super(length);
	}

	public MLocalByRef(Tokens store) {
		super(store);
	}

	@Override
	public Node getNode() {
		StringPiece name = this.getToken(1).toValue();
		Local local = new Local(name);
		return new LocalReference(local);
	}
}
