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

package com.raygroupintl.m.tool.entry.assumedvariables;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.m.tool.entry.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;

public class AssumedVariablesTool extends MEntryTool<AssumedVariables, IndexedFanout, AssumedVariablesBlockData> {
	private static class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<IndexedFanout, AssumedVariablesBlockData> {
		@Override
		public AssumedVariablesRecorder getRecorder() {
			return new AssumedVariablesRecorder();
		}
	}

	private static class AVTDataAggregator extends RecursiveDataAggregator<Set<String>, IndexedFanout, AssumedVariablesBlockData> {
		public AVTDataAggregator(Block<IndexedFanout, AssumedVariablesBlockData> block, BlocksSupply<Block<IndexedFanout, AssumedVariablesBlockData>> supply) {
			super(block, supply);
		}
		
		protected Set<String> getNewDataInstance(Block<IndexedFanout, AssumedVariablesBlockData> block) {
			AssumedVariablesBlockData bd = block.getData();
			return new HashSet<>(bd.getAssumedLocals());		
		}
		
		protected int updateData(Block<IndexedFanout, AssumedVariablesBlockData> targetBlock, Set<String> targetData, Set<String> sourceData, IndexedFanout property) {
			AssumedVariablesBlockData bd = targetBlock.getData();		
			int result = 0;
			for (String name : sourceData) {
				if (! bd.isDefined(name, property.getIndex())) {
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
	protected BlockRecorderFactory<IndexedFanout, AssumedVariablesBlockData> getBlockRecorderFactory() {
		return new EntryCodeInfoRecorderFactory();
	}

	@Override
	public AssumedVariables getResult(Block<IndexedFanout, AssumedVariablesBlockData> block, Filter<EntryId> filter) {
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
