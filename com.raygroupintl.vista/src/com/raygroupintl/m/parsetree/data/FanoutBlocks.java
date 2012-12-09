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

public class FanoutBlocks<T> {
	private Map<Integer, FaninList<T>> faninListMap = new HashMap<Integer, FaninList<T>>();
	private List<T> list = new ArrayList<T>();
	private List<T> storedList = new ArrayList<T>();
	private int rootId;
	private ObjectIdContainer blockIdContainer;
	
	public FanoutBlocks(T root, ObjectIdContainer blockIdContainer) {
		this.list.add(root);
		this.rootId = System.identityHashCode(root);
		FaninList<T> faninList = new FaninList<T>(root);
		this.faninListMap.put(this.rootId, faninList);
		this.blockIdContainer = blockIdContainer;
	}
	
	public void add(T fanin, T fanout, int fanoutIndex) {
		Integer fanoutId = System.identityHashCode(fanout);
		boolean stored = this.blockIdContainer == null ? false : this.blockIdContainer.contains(fanoutId);
		if (stored) {
			FaninList<T> faninList = this.faninListMap.get(fanoutId);
			if (faninList == null) {
				this.storedList.add(fanout);
				faninList = new FaninList<T>(fanout);
				this.faninListMap.put(fanoutId, faninList);						
			}					
			faninList.addFanin(fanin, fanoutIndex);
		} else {
			FaninList<T> faninList = this.faninListMap.get(fanoutId);
			if (faninList == null) {
				this.list.add(fanout);
				faninList = new FaninList<T>(fanout);
				this.faninListMap.put(fanoutId, faninList);
			}
			faninList.addFanin(fanin, fanoutIndex);
		}
	}
	
	public T getBlock(int index) {
		if (index < this.list.size()) {
			return this.list.get(index);
		} else {
			return null;
		}
	}
	
	public int getSize() {
		return this.list.size();
	}
	
	public FaninList<T> getFaninList(int id) {
		return this.faninListMap.get(id);	
	}
	
	public FaninList<T> getFaninList(T block) {
		int id = System.identityHashCode(block);
		return this.faninListMap.get(id);	
	}
	
	public List<T> getBlocks() {
		return this.list;
	}
	
	public List<T> getEvaludatedBlocks() {
		return this.storedList;
	}
}
	
