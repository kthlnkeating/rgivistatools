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

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;

public class BasicCodeInfoAccumulator extends Accumulator<BasicCodeInfoTR, CodeInfo> {
	public BasicCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply) {
		super(blocksSupply);
	}

	public BasicCodeInfoAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		super(blocksSupply, filterFactory);
	}
	
	@Override
	protected BasicCodeInfoTR getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(block, blocksSupply);
		BasicCodeInfo apiData = bcia.get(filter);
		return new BasicCodeInfoTR(block.getAttachedObject().getFormals(), apiData);
	}
	
	@Override
	protected BasicCodeInfoTR getEmptyBlockResult(EntryId entryId) {
		return new BasicCodeInfoTR(null, null);		
	}
}
