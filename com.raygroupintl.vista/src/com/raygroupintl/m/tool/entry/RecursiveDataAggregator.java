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

package com.raygroupintl.m.tool.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FaninList;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Indexed;

public abstract class RecursiveDataAggregator<T, U extends BlockData> {
	Block<U> block;
	BlocksSupply<Block<U>> supply;
	
	public RecursiveDataAggregator(Block<U> block, BlocksSupply<Block<U>> supply) {
		this.block = block;
		this.supply = supply;
	}
	
	protected abstract T getNewDataInstance(Block<U> block);
	
	protected abstract int updateData(Block<U> targetBlock, T targetData, T sourceData, int index);
	
	private int updateFaninData(T data, Block<U> b, FanoutBlocks<Block<U>> fanoutBlocks, Map<Integer, T> datas) {
		int numChange = 0;
		FaninList<Block<U>> faninList = fanoutBlocks.getFaninList(b);
		List<Indexed<Block<U>>> faninBlocks = faninList.getFanins();
		for (Indexed<Block<U>> ib : faninBlocks) {
			Block<U> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			T faninData = datas.get(faninId);
			numChange += this.updateData(faninBlock, faninData, data, ib.getIndex());
		}		
		return numChange;
	}
	
	private T get(FanoutBlocks<Block<U>> fanoutBlocks, DataStore<T> store) {			
		Map<Integer, T> datas = new HashMap<Integer, T>();

		List<Block<U>> blocks = fanoutBlocks.getBlocks();
		for (Block<U> b : blocks) {
			int id = System.identityHashCode(b);
			T data = this.getNewDataInstance(b);
			datas.put(id, data);
		}
		
		List<Block<U>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<U> b : evaluatedBlocks) {
			T data = store.get(b);
			this.updateFaninData(data, b, fanoutBlocks, datas);
		}
		
		int totalChange = Integer.MAX_VALUE;

		while (totalChange > 0) {
			totalChange = 0;
			for (int i=blocks.size()-1; i>=0; --i) {
				Block<U> b = blocks.get(i);
				int id = System.identityHashCode(b);
				T data = datas.get(id);
				totalChange += this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<U> bi : blocks) {
			store.put(bi, datas);
		}
		Block<U> b = blocks.get(0);
		return store.put(b, datas);
	}
		
	public T get(DataStore<T> store, Filter<EntryId> filter, Set<EntryId> missing) {
		T result = store.get(this.block);
		if (result != null) {
			return result;
		}
		FanoutBlocks<Block<U>> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter, missing);
		return this.get(fanoutBlocks, store);
	}
}
