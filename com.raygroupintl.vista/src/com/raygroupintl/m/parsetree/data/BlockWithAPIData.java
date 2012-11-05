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
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.struct.Indexed;

public class BlockWithAPIData extends Block<BlockAPIData> {
	private BlockAPIData staticData = new BlockAPIData();
	
	public BlockWithAPIData(int index, EntryId entryId, Blocks<BlockAPIData> siblings) {
		super(index, entryId, siblings);
	}

	public APIData getAPI(FanoutBlocks<BlockAPIData> fanoutBlocks, DataStore<APIData> apiDataStore) {			
		List<Block<BlockAPIData>> blocks = fanoutBlocks.getBlocks();
		List<Block<BlockAPIData>> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		
		Map<Integer, APIData> datas = new HashMap<Integer, APIData>();
		for (Block<BlockAPIData> b : blocks) {
			int id = System.identityHashCode(b);
			APIData data = b.getData().getAPIData();
			datas.put(id, data);
		}
		
		for (Block<BlockAPIData> b : evaluatedBlocks) {
			APIData data = apiDataStore.get(b);
			FaninList<BlockAPIData> faninList = fanoutBlocks.getFaninList(b);
			List<Indexed<Block<BlockAPIData>>> faninBlocks = faninList.getFaninBlocks();
			for (Indexed<Block<BlockAPIData>> ib : faninBlocks) {
				Block<BlockAPIData> faninBlock = ib.getObject();
				int faninId = System.identityHashCode(faninBlock);
				APIData faninData = datas.get(faninId);
				faninData.mergeAccumulative(faninBlock.getData(), data, ib.getIndex());
			}
		}
		
		int totalChange = Integer.MAX_VALUE;

		while (totalChange > 0) {
			totalChange = 0;
			for (int i=blocks.size()-1; i>=0; --i) {
				Block<BlockAPIData> b = blocks.get(i);
				int id = System.identityHashCode(b);
				APIData data = datas.get(id);
				FaninList<BlockAPIData> faninList = fanoutBlocks.getFaninList(id);
				List<Indexed<Block<BlockAPIData>>> faninBlocks = faninList.getFaninBlocks();
				for (Indexed<Block<BlockAPIData>> ib : faninBlocks) {
					Block<BlockAPIData> faninBlock = ib.getObject();
					int faninId = System.identityHashCode(faninBlock);
					APIData faninData = datas.get(faninId);
					totalChange += faninData.mergeAccumulative(faninBlock.getData(), data, ib.getIndex());
				}
			}
		}
					
		for (Block<BlockAPIData> bi : blocks) {
			apiDataStore.put(bi, datas);
		}
		Block<BlockAPIData> b = blocks.get(0);
		return apiDataStore.put(b, datas);
	}
		
	public APIData getAssumedLocals(BlocksSupply<BlockAPIData> blocksSupply, DataStore<APIData> store, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.getEntryId());
		APIData result = store.get(this);
		if (result != null) {
			return result;
		}
		FanoutBlocks<BlockAPIData> fanoutBlocks = this.getFanoutBlocks(blocksSupply, store, filter, replacedRoutines);
		return this.getAPI(fanoutBlocks, store);
	}
	
	public APIData getAPIData(BlocksSupply<BlockAPIData> blocksSupply, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.getEntryId());
		APIData result = new APIData();
		FanoutBlocks<BlockAPIData> fanoutBlocks = this.getFanoutBlocks(blocksSupply, filter, replacedRoutines);
		List<Block<BlockAPIData>> blocks = fanoutBlocks.getBlocks();
		for (Block<BlockAPIData> b : blocks) {
			result.mergeAdditive(b.getData());
		}
		return result;		
	}
	
	@Override
	public BlockAPIData getData() {
		return this.staticData;
	}
}
