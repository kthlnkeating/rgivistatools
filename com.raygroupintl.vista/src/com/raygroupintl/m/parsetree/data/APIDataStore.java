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

package com.raygroupintl.m.parsetree.data;

import java.util.HashMap;
import java.util.Map;

public class APIDataStore {
	private Map<Integer, APIData> map = new HashMap<Integer, APIData>();

	public void reset() {
		this.map = new HashMap<Integer, APIData>();
	}

	public APIData get(Block block) {
		int id = System.identityHashCode(block);
		return this.map.get(id);
	}

	public void put(APIData data) {
		Block block = data.getSourceBlock();
		int id = System.identityHashCode(block);
		this.map.put(id, data);
	}
}
