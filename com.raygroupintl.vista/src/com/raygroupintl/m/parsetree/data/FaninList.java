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

import com.raygroupintl.struct.Indexed;

public class FaninList<T> {
	private Block<T> block;
	private List<Indexed<Block<T>>> faninBlocks = new ArrayList<Indexed<Block<T>>>();
	private Set<Integer> existing = new HashSet<Integer>();
	
	public FaninList(Block<T> block) {
		this.block = block;
	}
			
	public void addFanin(Block<T> faninBlock, int index) {
		int faninId = System.identityHashCode(faninBlock);
		if (faninId != System.identityHashCode(this.block)) {
			if (! this.existing.contains(faninId)) {
				Indexed<Block<T>> e = new Indexed<Block<T>>(faninBlock, index);
				this.faninBlocks.add(e);
				this.existing.add(faninId);
			}
		}
	}
	
	public List<Indexed<Block<T>>> getFaninBlocks() {
		return this.faninBlocks;
	}
}
