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

package com.raygroupintl.m.tool.entry.fanout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlockData;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.struct.Filter;

public class FanoutTool extends MEntryTool<EntryFanouts, IndexedFanout, BlockData<IndexedFanout>>{
	private static class EntryFanoutRecorderFactory implements BlockRecorderFactory<IndexedFanout, BlockData<IndexedFanout>> {
		@Override
		public VoidBlockRecorder getRecorder() {
			return new VoidBlockRecorder();
		}
	}

	public FanoutTool(CommonToolParams params) {
		super(params);
	}
	
	@Override
	protected EntryFanoutRecorderFactory getBlockRecorderFactory() {
		return new EntryFanoutRecorderFactory();
	}

	@Override
	protected EntryFanouts getResult(Block<IndexedFanout, BlockData<IndexedFanout>> block, Filter<Fanout> filter) {
		Set<EntryId> missing = new HashSet<EntryId>();
		FanoutBlocks<IndexedFanout, BlockData<IndexedFanout>> fanoutBlocks = block.getFanoutBlocks(this.blocksSupply, filter, missing);
		List<Block<IndexedFanout, BlockData<IndexedFanout>>> blocks = fanoutBlocks.getBlocks();
		boolean first = true;
		EntryFanouts result = new EntryFanouts();
		for (Block<IndexedFanout, BlockData<IndexedFanout>> b : blocks) {
			if (first) {
				first = false;
			} else if (! b.isInternal()) {					
				result.add(b.getEntryId());
			}
		}
		return result;
	}
	
	@Override
	protected EntryFanouts getEmptyBlockResult(EntryId entryId) {
		EntryFanouts result = new EntryFanouts();
		result.setErrorMsg("Not Found");
		return result;
	}
}
