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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.struct.ObjectIdContainer;

public class FanoutBlocks {
	private Map<Integer, FaninList> faninListMap = new HashMap<Integer, FaninList>();
	private List<Block> list = new ArrayList<Block>();
	private List<Block> storedList = new ArrayList<Block>();
	private int rootId;
	private ObjectIdContainer blockIdContainer;
	
	public FanoutBlocks(Block root, ObjectIdContainer blockIdContainer) {
		this.list.add(root);
		this.rootId = System.identityHashCode(root);
		FaninList faninList = new FaninList(root);
		this.faninListMap.put(this.rootId, faninList);
		this.blockIdContainer = blockIdContainer;
	}
	
	public void add(Block fanin, Block fanout, int fanoutIndex) {
		Integer fanoutId = System.identityHashCode(fanout);
		boolean stored = this.blockIdContainer == null ? false : this.blockIdContainer.contains(fanoutId);
		if (stored) {
			FaninList faninList = this.faninListMap.get(fanoutId);
			if (faninList == null) {
				this.storedList.add(fanout);
				faninList = new FaninList(fanout);
				this.faninListMap.put(fanoutId, faninList);						
			}					
			faninList.addFanin(fanin, fanoutIndex);
		} else {
			FaninList faninList = this.faninListMap.get(fanoutId);
			if (faninList == null) {
				this.list.add(fanout);
				faninList = new FaninList(fanout);
				this.faninListMap.put(fanoutId, faninList);
			}
			faninList.addFanin(fanin, fanoutIndex);
		}
	}
	
	public Block getBlock(int index) {
		if (index < this.list.size()) {
			return this.list.get(index);
		} else {
			return null;
		}
	}
	
	public int getSize() {
		return this.list.size();
	}
	
	public FaninList getFaninList(int id) {
		return this.faninListMap.get(id);	
	}
	
	public FaninList getFaninList(Block block) {
		int id = System.identityHashCode(block);
		return this.faninListMap.get(id);	
	}
	
	public List<Block> getBlocks() {
		return this.list;
	}
	
	public List<Block> getEvaludatedBlocks() {
		return this.storedList;
	}
			
	public List<Block> getAllBlocks() {
		List<Block> result = new ArrayList<Block>(this.list);
		result.addAll(this.storedList);
		return result;
	}	
}
	
