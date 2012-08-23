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

package com.raygroupintl.m.struct;

import java.util.HashMap;

public class MNameWithMnemonic {
	private String mnemonic;
	private String name;
	
	public MNameWithMnemonic(String mnemonic, String name) {
		this.mnemonic = mnemonic;
		this.name = name;
	}
	
	public String getMnemonic() {
		return this.mnemonic;
	}
	
	public String getName() {
		return this.name;
	}
	
	@SuppressWarnings("serial")
	public static class Map extends HashMap<String, MNameWithMnemonic> {
		public void update(String mnemonic, String name) {
			MNameWithMnemonic iv = new MNameWithMnemonic(mnemonic, name);
			this.put(mnemonic, iv);
			if (! mnemonic.equals(name)) {
				this.put(name, iv);			
			}
		}
	}
}
