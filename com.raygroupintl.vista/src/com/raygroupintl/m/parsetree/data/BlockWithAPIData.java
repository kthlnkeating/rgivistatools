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

public class BlockWithAPIData extends Block {
	private BlockAPIData staticData = new BlockAPIData();
	
	public BlockWithAPIData(int index, EntryId entryId, Blocks siblings) {
		super(index, entryId, siblings);
	}

	public APIData getAPI(FanoutBlocks fanoutBlocks, DataStore<APIData> apiDataStore) {			
		List<Block> blocks = fanoutBlocks.getBlocks();
		List<Block> evaluatedBlocks = fanoutBlocks.getEvaludatedBlocks();
		
		Map<Integer, APIData> datas = new HashMap<Integer, APIData>();
		for (Block b : blocks) {
			int id = System.identityHashCode(b);
			APIData data = b.getInitialAccumulativeData();
			datas.put(id, data);
		}
		
		for (Block b : evaluatedBlocks) {
			APIData data = apiDataStore.get(b);
			FaninList faninList = fanoutBlocks.getFaninList(b);
			List<Indexed<Block>> faninBlocks = faninList.getFaninBlocks();
			for (Indexed<Block> ib : faninBlocks) {
				Block faninBlock = ib.getObject();
				int faninId = System.identityHashCode(faninBlock);
				APIData faninData = datas.get(faninId);
				faninBlock.mergeAccumulative(faninData, data, ib.getIndex());
			}
		}
		
		int totalChange = Integer.MAX_VALUE;

		while (totalChange > 0) {
			totalChange = 0;
			for (int i=blocks.size()-1; i>=0; --i) {
				Block b = blocks.get(i);
				int id = System.identityHashCode(b);
				APIData data = datas.get(id);
				FaninList faninList = fanoutBlocks.getFaninList(id);
				List<Indexed<Block>> faninBlocks = faninList.getFaninBlocks();
				for (Indexed<Block> ib : faninBlocks) {
					Block faninBlock = ib.getObject();
					int faninId = System.identityHashCode(faninBlock);
					APIData faninData = datas.get(faninId);
					totalChange += faninBlock.mergeAccumulative(faninData, data, ib.getIndex());
				}
			}
		}
					
		for (Block bi : blocks) {
			apiDataStore.put(bi, datas);
		}
		Block b = blocks.get(0);
		return apiDataStore.put(b, datas);
	}
		
	public APIData getAssumedLocals(BlocksSupply blocksSupply, DataStore<APIData> store, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.getEntryId());
		APIData result = store.get(this);
		if (result != null) {
			return result;
		}
		FanoutBlocks fanoutBlocks = this.getFanoutBlocks(blocksSupply, store, filter, replacedRoutines);
		return this.getAPI(fanoutBlocks, store);
	}
	
	public APIData getAPIData(BlocksSupply blocksSupply, SourcedFanoutFilter filter, Map<String, String> replacedRoutines) {
		if (filter != null) filter.setSource(this.getEntryId());
		APIData result = this.getInitialAdditiveData();
		FanoutBlocks fanoutBlocks = this.getFanoutBlocks(blocksSupply, filter, replacedRoutines);
		List<Block> blocks = fanoutBlocks.getAllBlocks();
		for (Block b : blocks) {
			b.mergeAdditiveTo(result);
		}
		return result;		
	}
	
	public BlockAPIData getStaticData() {
		return this.staticData;
	}
		
	@Override
	public APIData getInitialAccumulativeData() {
		return this.staticData.getDynamicData();
	}
		
	@Override
	public APIData getInitialAdditiveData() {
		return new APIData();
	}
		
	@Override
	public int mergeAccumulative(APIData target, APIData source, int sourceIndex) {
		return target.mergeAccumulative(this.getStaticData(), source, sourceIndex);
	}
	
	@Override
	public void mergeAdditiveTo(APIData target) {
		target.mergeAdditive(this.getStaticData());		
	}
}
