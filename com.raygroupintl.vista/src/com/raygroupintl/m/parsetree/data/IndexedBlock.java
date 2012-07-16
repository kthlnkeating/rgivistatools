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

import java.util.List;

import com.raygroupintl.struct.Indexed;

public class IndexedBlock {
	private int index;
	private Block block;
	private List<Indexed<String>> byRefs;
		
	public IndexedBlock(int index, Block block, List<Indexed<String>> byRefs) {
		this.index = index;
		this.block = block;
		this.byRefs = byRefs;
	}

	public int getIndex() {
		return this.index;
	}
		
	public Block getBlock() {
		return this.block;
	}
	
	public List<Indexed<String>> getByRefs() {
		return this.byRefs;
	}
}
