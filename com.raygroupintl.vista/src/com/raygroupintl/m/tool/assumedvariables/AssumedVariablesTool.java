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
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;

public class AssumedVariablesTool extends Accumulator<AssumedVariables, CodeInfo> {
	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private AssumedVariablesToolParams params;
	
	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		this(blocksSupply, new AssumedVariablesToolParams());
	}

	public AssumedVariablesTool(BlocksSupply<Block<CodeInfo>> blocksSupply, AssumedVariablesToolParams params) {
		super(blocksSupply, params.getRecursionSpecification().getFanoutFilterFactory());
		this.params = params;
	}
	
	@Override
	public AssumedVariables getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(block, blocksSupply);
		Set<String> assumedVariables = ala.get(this.store, filter);
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
