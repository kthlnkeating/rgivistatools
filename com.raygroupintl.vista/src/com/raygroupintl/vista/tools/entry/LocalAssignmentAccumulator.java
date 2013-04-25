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

package com.raygroupintl.vista.tools.entry;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;

public class LocalAssignmentAccumulator extends Accumulator<CodeLocations, CodeLocations> {
	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply) {
		super(blocksSupply);
	}

	public LocalAssignmentAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory);
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
