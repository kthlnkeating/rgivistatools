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

package com.raygroupintl.m.tool.fanout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.MEntryTool;
import com.raygroupintl.struct.Filter;

public class FanoutTool extends MEntryTool<EntryFanouts, Void>{
	private static class EntryFanoutRecorderFactory implements BlockRecorderFactory<Void> {
		@Override
		public BlockRecorder<Void> getRecorder() {
			return new VoidBlockRecorder();
		}
	}

	public FanoutTool(CommonToolParams params) {
		super(params);
	}
	
	@Override
	protected BlockRecorderFactory<Void> getBlockRecorderFactory() {
		return new EntryFanoutRecorderFactory();
	}

	@Override
	protected EntryFanouts getResult(Block<Void> block, Filter<EntryId> filter) {
		Set<EntryId> missing = new HashSet<EntryId>();
		FanoutBlocks<Block<Void>> fanoutBlocks = block.getFanoutBlocks(this.blocksSupply, filter, missing);
		List<Block<Void>> blocks = fanoutBlocks.getBlocks();
		boolean first = true;
		EntryFanouts result = new EntryFanouts();
		for (Block<Void> b : blocks) {
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
