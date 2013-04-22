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
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class EntryCodeInfoAccumulator extends Accumulator<EntryCodeInfo, CodeInfo> {
	private AssumedVariablesTool assumedVariableAccumulator;
	private BasicCodeInfoAccumulator basicCodeInfoAccumulator;
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		super(blocksSupply, new ToolResultCollection<EntryCodeInfo>());
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply);
	}

	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory, new ToolResultCollection<EntryCodeInfo>());
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply, filterFactory);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply, filterFactory);
	}
	
	@Override
	protected EntryCodeInfo getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		AssumedVariablesTR assumedVariables = this.assumedVariableAccumulator.getResult(block, filter);
		BasicCodeInfoTR basicCodeInfo = this.basicCodeInfoAccumulator.getResult(block, filter);
		return new EntryCodeInfo(block.getEntryId(), block.getAttachedObject().getFormals(), assumedVariables, basicCodeInfo);		
	}
	
	@Override
	protected EntryCodeInfo getEmptyBlockResult(EntryId entryId) {
		return new EntryCodeInfo(entryId, null, null, null);		
	}
}
