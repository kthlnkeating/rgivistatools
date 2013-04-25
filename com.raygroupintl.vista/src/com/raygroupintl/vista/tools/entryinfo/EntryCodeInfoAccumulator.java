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

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.struct.Filter;

public class EntryCodeInfoAccumulator extends Accumulator<EntryCodeInfo, CodeInfo> {
	private AssumedVariablesTool assumedVariableAccumulator;
	private BasicCodeInfoAccumulator basicCodeInfoAccumulator;
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		super(blocksSupply);
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply);
	}

	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, AssumedVariablesToolParams params) {
		super(blocksSupply, params.getRecursionSpecification().getFanoutFilterFactory());
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply, params);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply, params.getRecursionSpecification().getFanoutFilterFactory());
	}
	
	@Override
	protected EntryCodeInfo getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		AssumedVariables assumedVariables = this.assumedVariableAccumulator.getResult(block, filter);
		BasicCodeInfoTR basicCodeInfo = this.basicCodeInfoAccumulator.getResult(block, filter);
		return new EntryCodeInfo(block.getAttachedObject().getFormals(), assumedVariables, basicCodeInfo);		
	}
	
	@Override
	protected EntryCodeInfo getEmptyBlockResult(EntryId entryId) {
		return new EntryCodeInfo(null, null, null);		
	}
}
