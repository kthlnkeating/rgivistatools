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

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;

public class EntryCodeInfoAccumulator {
	private AssumedVariablesTool assumedVariableAccumulator;
	private BasicCodeInfoAccumulator basicCodeInfoAccumulator;
	
	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply);
	}

	public EntryCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, AssumedVariablesToolParams params) {
		this.assumedVariableAccumulator = new AssumedVariablesTool(blocksSupply, params);
		this.basicCodeInfoAccumulator = new BasicCodeInfoAccumulator(blocksSupply, params.getRecursionSpecification().getFanoutFilterFactory());
	}
	
	protected EntryCodeInfo getResult(EntryId entryId) {
		AssumedVariables assumedVariables = this.assumedVariableAccumulator.getResult(entryId);		
		BasicCodeInfoTR basicCodeInfo = this.basicCodeInfoAccumulator.getResult(entryId);
		if (assumedVariables.isValid() && basicCodeInfo.isValid()) {
			return new EntryCodeInfo(assumedVariables, basicCodeInfo);					
		} else {
			return new EntryCodeInfo(null, null);		
		}		
	}

	public List<EntryCodeInfo> getResult(List<EntryId> entries) {
		List<EntryCodeInfo> result = new ArrayList<EntryCodeInfo>();
		for (EntryId entryId : entries) {
			EntryCodeInfo e = this.getResult(entryId);
			result.add(e);
		}		
		return result;
	}
}
