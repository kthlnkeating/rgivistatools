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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.struct.Indexed;

public class AssumedLocalAggregator {
	Block<BlockCodeInfo> block;
	BlocksSupply<BlockCodeInfo> supply;
	
	public AssumedLocalAggregator(Block<BlockCodeInfo> block, BlocksSupply<BlockCodeInfo> supply) {
		this.block = block;
		this.supply = supply;
	}
	
	private int updateFaninData(Set<String> data, Block<BlockCodeInfo> b, FanoutBlocks<BlockCodeInfo> fanoutBlocks, Map<Integer, Set<String>> datas) {
		int numChange = 0;
		FaninList<BlockCodeInfo> faninList = fanoutBlocks.getFaninList(b);
		List<Indexed<Block<BlockCodeInfo>>> faninBlocks = faninList.getFaninBlocks();
		for (Indexed<Block<BlockCodeInfo>> ib : faninBlocks) {
			Block<BlockCodeInfo> faninBlock = ib.getObject();
			int faninId = System.identityHashCode(faninBlock);
			Set<String> faninData = datas.get(faninId);
			numChange += faninBlock.getData().updateAssumed(faninData, data, ib.getIndex());
		}		
		return numChange;
	}
	
	private Set<String> getAssumedLocals(FanoutBlocks<BlockCodeInfo> fanoutBlocks, DataStore<Set<String>> apiDataStore) {			
		Map<Integer, Set<String>> datas = new HashMap<Integer, Set<String>>();

		List<Block<BlockCodeInfo>> blocks = fanoutBlocks.getBlocks();
		for (Block<BlockCodeInfo> b : blocks) {
			int id = System.identityHashCode(b);
			BlockCodeInfo bd = b.getData();
			Set<String> data = new HashSet<>(bd.getAssumedLocals());
			datas.put(id, data);
		}
		
		List<Block<BlockCodeInfo>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		for (Block<BlockCodeInfo> b : evaluatedBlocks) {
			Set<String> data = apiDataStore.get(b);
			this.updateFaninData(data, b, fanoutBlocks, datas);
		}
		
		int totalChange = Integer.MAX_VALUE;

		while (totalChange > 0) {
			totalChange = 0;
			for (int i=blocks.size()-1; i>=0; --i) {
				Block<BlockCodeInfo> b = blocks.get(i);
				int id = System.identityHashCode(b);
				Set<String> data = datas.get(id);
				totalChange += this.updateFaninData(data, b, fanoutBlocks, datas);
			}
		}
					
		for (Block<BlockCodeInfo> bi : blocks) {
			apiDataStore.put(bi, datas);
		}
		Block<BlockCodeInfo> b = blocks.get(0);
		return apiDataStore.put(b, datas);
	}
		
	public Set<String> getAssumedLocals(DataStore<Set<String>> store, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.block.getEntryId());
		Set<String> result = store.get(this);
		if (result != null) {
			return result;
		}
		FanoutBlocks<BlockCodeInfo> fanoutBlocks = this.block.getFanoutBlocks(this.supply, store, filter, replacedRoutines);
		return this.getAssumedLocals(fanoutBlocks, store);
	}
}
