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

import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.entryinfo.AssumedVariablesTR;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;

public class AssumedVariablesTool extends Accumulator<AssumedVariablesTR, CodeInfo> {
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private AVSTResultPresentation flags;
	
	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply, AVSTResultPresentation flags) {
		super(blocksSupply);
		this.flags = flags;
	}

	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		this(blocksSupply, new AVSTResultPresentation());
	}

	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory, AVSTResultPresentation flags) {
		super(blocksSupply, filterFactory);
		this.flags = flags;
	}
	
	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		this(blocksSupply, filterFactory, new AVSTResultPresentation());
	}
	
	@Override
	public AssumedVariablesTR getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(block, blocksSupply);
		Set<String> assumedVariables = ala.get(this.store, filter);
		if (assumedVariables != null) {
			assumedVariables.removeAll(this.flags.getExpected());
		}
		return new AssumedVariablesTR(block.getEntryId(), assumedVariables, this.flags);	
	}
	
	@Override
	protected AssumedVariablesTR getEmptyBlockResult(EntryId entryId) {
		return new AssumedVariablesTR(entryId, null, this.flags);		
	}
}
