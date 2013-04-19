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

package com.raygroupintl.vista.tools.entryinfo;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class AssumedVariableAccumulator extends Accumulator<AssumedVariablesTR, CodeInfo> {
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private AssumedVarsToolFlag flags;
	
	public AssumedVariableAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, AssumedVarsToolFlag flags) {
		super(blocksSupply, new ToolResultCollection<AssumedVariablesTR>());
		this.flags = flags;
	}

	public AssumedVariableAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		this(blocksSupply, new AssumedVarsToolFlag());
	}

	public AssumedVariableAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory, AssumedVarsToolFlag flags) {
		super(blocksSupply, filterFactory, new ToolResultCollection<AssumedVariablesTR>());
		this.flags = flags;
	}
	
	public AssumedVariableAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		this(blocksSupply, filterFactory, new AssumedVarsToolFlag());
	}
	
	@Override
	protected AssumedVariablesTR getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(block, blocksSupply);
		Set<String> assumedVariables = ala.get(this.store, filter);
		if (assumedVariables != null) {
			assumedVariables.removeAll(this.flags.getExpectedAssumedVariables());
		}
		return new AssumedVariablesTR(block.getEntryId(), assumedVariables, this.flags);	
	}
	
	@Override
	protected AssumedVariablesTR getEmptyBlockResult(EntryId entryId) {
		return new AssumedVariablesTR(entryId, null, this.flags);		
	}
}
