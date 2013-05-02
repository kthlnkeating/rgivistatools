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

package com.raygroupintl.m.tool.entry.localassignment;

import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.entry.AdditiveDataAggregator;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeLocations;
import com.raygroupintl.struct.Filter;

public class LocalAssignmentTool extends MEntryTool<CodeLocations, IndexedFanout, CodeLocations> {
	private class EntryLocalAssignmentRecorderFactory implements BlockRecorderFactory<IndexedFanout, CodeLocations> {
		@Override
		public LocalAssignmentRecorder getRecorder() {
			return new LocalAssignmentRecorder(LocalAssignmentTool.this.localsUnderTest);
		}
	}
	
	private class LATDataAggregator extends AdditiveDataAggregator<CodeLocations, IndexedFanout, CodeLocations> {
		public LATDataAggregator(Block<IndexedFanout, CodeLocations> block, BlocksSupply<Block<IndexedFanout, CodeLocations>> supply) {
			super(block, supply);
		}
		
		protected CodeLocations getNewDataInstance(Block<IndexedFanout, CodeLocations> block) {
			return new CodeLocations(block.getEntryId());
		}
		
		protected void updateData(CodeLocations targetData, Block<IndexedFanout, CodeLocations> fanoutBlock) {
			CodeLocations source = fanoutBlock.getData();
			List<CodeLocation> cls = source.getCodeLocations();
			if (cls != null) {
				for (CodeLocation c : cls) {
					targetData.add(c);
				}
			}
		}		
	}

	private Set<String> localsUnderTest;
	
	public LocalAssignmentTool(LocalAssignmentToolParams params) {
		super(params);
		this.localsUnderTest = params.getLocals();
	}
	
	@Override
	protected EntryLocalAssignmentRecorderFactory getBlockRecorderFactory() {
		return this.new EntryLocalAssignmentRecorderFactory();
	}

	@Override
	protected CodeLocations getResult(Block<IndexedFanout, CodeLocations> block, Filter<Fanout> filter, Set<EntryId> missingEntryIds) {
		LATDataAggregator bcia = new LATDataAggregator(block, blocksSupply);
		return bcia.get(filter, missingEntryIds);
	}
}
