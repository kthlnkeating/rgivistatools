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

package com.raygroupintl.m.tool.localassignment;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.MEntryTool;
import com.raygroupintl.m.tool.basiccodeinfo.CodeLocations;
import com.raygroupintl.struct.Filter;

public class LocalAssignmentTool extends MEntryTool<CodeLocations, CodeLocations> {
	private class EntryLocalAssignmentRecorderFactory implements BlockRecorderFactory<CodeLocations> {
		@Override
		public BlockRecorder<CodeLocations> getRecorder() {
			return new LocalAssignmentRecorder(LocalAssignmentTool.this.localsUnderTest);
		}
	}

	private Set<String> localsUnderTest;
	
	public LocalAssignmentTool(LocalAssignmentToolParams params) {
		super(params);
		this.localsUnderTest = params.getLocals();
	}
	
	@Override
	protected BlockRecorderFactory<CodeLocations> getBlockRecorderFactory() {
		return this.new EntryLocalAssignmentRecorderFactory();
	}

	@Override
	protected CodeLocations getResult(Block<CodeLocations> block, Filter<EntryId> filter) {
		AdditiveDataAggregator<CodeLocations, CodeLocations> bcia = new AdditiveDataAggregator<CodeLocations, CodeLocations>(block, blocksSupply);
		return bcia.get(filter);
	}
	
	@Override
	protected CodeLocations getEmptyBlockResult(EntryId entryId) {
		return new CodeLocations();
	}
}
