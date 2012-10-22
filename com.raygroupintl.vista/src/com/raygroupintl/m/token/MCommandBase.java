//---------------------------------------------------------------------------
//Copyright 2012 Ray Group International
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.m.token;

import com.raygroupintl.parser.TextPiece;

public abstract class MCommandBase extends MSequence {
	public MCommandBase(MToken token0, MToken token1) {
		super(token0, token1);
	}

	protected abstract String getFullName();

	protected MToken getArgument() {
		MToken argument = this.getToken(1, 2);
		if ((argument == null) || (argument.toValue().length() == 0)) {
			return null;
		}
		return argument;
	}

	@Override
	public void beautify() {
		TextPiece n = this.getToken(0).toValue();
		TextPiece newName = new TextPiece(getFullName());
		n.set(newName);
		super.beautify();
	}
}
