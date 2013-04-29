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

package com.raygroupintl.m.tool.assumedvariables;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.MEntryTool;
import com.raygroupintl.m.tool.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;

public class AssumedVariablesTool extends MEntryTool<AssumedVariables, AssumedVariablesBlockData> {
	private static class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<AssumedVariablesBlockData> {
		@Override
		public BlockRecorder<AssumedVariablesBlockData> getRecorder() {
			return new AssumedVariablesRecorder();
		}
	}

	private static class AVTDataAggregator extends RecursiveDataAggregator<Set<String>, AssumedVariablesBlockData> {
		public AVTDataAggregator(Block<AssumedVariablesBlockData> block, BlocksSupply<Block<AssumedVariablesBlockData>> supply) {
			super(block, supply);
		}
		
		protected Set<String> getNewDataInstance(Block<AssumedVariablesBlockData> block) {
			AssumedVariablesBlockData bd = block.getAttachedObject();
			return new HashSet<>(bd.getAssumedLocals());		
		}
		
		protected int updateData(Block<AssumedVariablesBlockData> targetBlock, Set<String> targetData, Set<String> sourceData, int index) {
			AssumedVariablesBlockData bd = targetBlock.getAttachedObject();		
			int result = 0;
			for (String name : sourceData) {
				if (! bd.isDefined(name, index)) {
					if (! targetData.contains(name)) {
						targetData.add(name);
						++result;
					}
				}
			}
			return result;
		}		
	}
	
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private AssumedVariablesToolParams params;
	
	public AssumedVariablesTool(AssumedVariablesToolParams params) {
		super(params);
		this.params = params;
	}
	
	@Override
	protected BlockRecorderFactory<AssumedVariablesBlockData> getBlockRecorderFactory() {
		return new EntryCodeInfoRecorderFactory();
	}

	@Override
	public AssumedVariables getResult(Block<AssumedVariablesBlockData> block, Filter<EntryId> filter) {
		AVTDataAggregator ala = new AVTDataAggregator(block, blocksSupply);
		Set<EntryId> missing = new HashSet<EntryId>();
		Set<String> assumedVariables = ala.get(this.store, filter, missing);
		if (assumedVariables != null) {
			assumedVariables.removeAll(this.params.getExpected());
		}
		return new AssumedVariables(assumedVariables);	
	}
	
	@Override
	protected AssumedVariables getEmptyBlockResult(EntryId entryId) {
		return new AssumedVariables(null);		
	}
}
