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
import com.raygroupintl.m.parsetree.data.EntryObject;
import com.raygroupintl.m.parsetree.data.FaninList;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.ObjectWithProperty;

public abstract class RecursiveDataAggregator<T, F extends EntryObject, U extends BlockData<F>> {
	Block<F, U> block;
	BlocksSupply<Block<F, U>> supply;
	
	public RecursiveDataAggregator(Block<F, U> block, BlocksSupply<Block<F, U>> supply) {
		this.block = block;
		this.supply = supply;
	}
	
	protected abstract T getNewDataInstance(Block<F, U> block);
	
	protected abstract int updateData(Block<F, U> targetBlock, T targetData, T sourceData, F sourceProperty);
	
	private int updateFaninData(T data, Block<F, U> b, FanoutBlocks<Block<F, U>, F> fanoutBlocks, Map<Integer, T> datas) {
		int numChange = 0;
		FaninList<Block<F, U>, F> faninList = fanoutBlocks.getFaninList(b);
		List<ObjectWithProperty<Block<F, U>, F>> faninBlocks = faninList.getFanins();
		for (ObjectWithProperty<Block<F, U>, F> ib : faninBlocks) {
			Block<F, U> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			T faninData = datas.get(faninId);
			numChange += this.updateData(faninBlock, faninData, data, ib.getProperty());
		}		
		return numChange;
	}
	
	private T get(FanoutBlocks<Block<F, U>, F> fanoutBlocks, DataStore<T> store) {			
		Map<Integer, T> datas = new HashMap<Integer, T>();

		List<Block<F, U>> blocks = fanoutBlocks.getBlocks();
		for (Block<F, U> b : blocks) {
			int id = System.identityHashCode(b);
			T data = this.getNewDataInstance(b);
			datas.put(id, data);
		}
		
		List<Block<F, U>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<F, U> b : evaluatedBlocks) {
			T data = store.get(b);
			this.updateFaninData(data, b, fanoutBlocks, datas);
		}
		
		int totalChange = Integer.MAX_VALUE;

		while (totalChange > 0) {
			totalChange = 0;
			for (int i=blocks.size()-1; i>=0; --i) {
				Block<F, U> b = blocks.get(i);
				int id = System.identityHashCode(b);
				T data = datas.get(id);
				totalChange += this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<F, U> bi : blocks) {
			store.put(bi, datas);
		}
		Block<F, U> b = blocks.get(0);
		return store.put(b, datas);
	}
		
	public T get(DataStore<T> store, Filter<EntryId> filter, Set<EntryId> missing) {
		T result = store.get(this.block);
		if (result != null) {
			return result;
		}
		FanoutBlocks<Block<F, U>, F> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter, missing);
		return this.get(fanoutBlocks, store);
	}
}
