//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.pwc.us.rgi.m.tool.entry.localassignment;

import java.util.List;
import java.util.Set;

import com.pwc.us.rgi.m.parsetree.data.EntryId;
import com.pwc.us.rgi.m.parsetree.data.Fanout;
import com.pwc.us.rgi.m.parsetree.visitor.BlockRecorderFactory;
import com.pwc.us.rgi.m.struct.CodeLocation;
import com.pwc.us.rgi.m.tool.entry.AdditiveDataAggregator;
import com.pwc.us.rgi.m.tool.entry.Block;
import com.pwc.us.rgi.m.tool.entry.BlocksSupply;
import com.pwc.us.rgi.m.tool.entry.MEntryTool;
import com.pwc.us.rgi.m.tool.entry.basiccodeinfo.CodeLocations;
import com.pwc.us.rgi.struct.Filter;

public class LocalAssignmentTool extends MEntryTool<CodeLocations, Fanout, CodeLocations> {
	private class EntryLocalAssignmentRecorderFactory implements BlockRecorderFactory<Fanout, CodeLocations> {
		@Override
		public LocalAssignmentRecorder getRecorder() {
			return new LocalAssignmentRecorder(LocalAssignmentTool.this.localsUnderTest);
		}
	}
	
	private class LATDataAggregator extends AdditiveDataAggregator<CodeLocations, Fanout, CodeLocations> {
		public LATDataAggregator(Block<Fanout, CodeLocations> block, BlocksSupply<Fanout, CodeLocations> supply) {
			super(block, supply);
		}
		
		protected CodeLocations getNewDataInstance(Block<Fanout, CodeLocations> block) {
			return new CodeLocations(block.getEntryId());
		}
		
		protected void updateData(CodeLocations targetData, CodeLocations fanoutData) {
			List<CodeLocation> cls = fanoutData.getCodeLocations();
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
	protected CodeLocations getResult(Block<Fanout, CodeLocations> block, Filter<Fanout> filter, Set<EntryId> missingEntryIds) {
		LATDataAggregator bcia = new LATDataAggregator(block, blocksSupply);
		return bcia.get(filter, missingEntryIds);
	}
}
