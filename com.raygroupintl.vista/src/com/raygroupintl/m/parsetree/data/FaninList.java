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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlockData;
import com.raygroupintl.struct.ObjectWithProperty;

public class FaninList<U extends Fanout, T extends BlockData<U>> {
	private Block<U, T> node;
	private List<ObjectWithProperty<Block<U, T>, U>> faninNodes = new ArrayList<ObjectWithProperty<Block<U, T>, U>>();
	private Set<Integer> existing = new HashSet<Integer>();
	
	public FaninList(Block<U, T> node) {
		this.node = node;
	}
			
	public void addFanin(Block<U, T> faninNode, U property) {
		int faninId = System.identityHashCode(faninNode);
		if (faninId != System.identityHashCode(this.node)) {
			if (! this.existing.contains(faninId)) {
				ObjectWithProperty<Block<U, T>, U> e = new ObjectWithProperty<Block<U, T>, U>(faninNode, property);
				this.faninNodes.add(e);
				this.existing.add(faninId);
			}
		}
	}
	
	public List<ObjectWithProperty<Block<U, T>, U>> getFanins() {
		return this.faninNodes;
	}
}
