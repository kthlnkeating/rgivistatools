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

package com.raygroupintl.m.tool;

import java.util.List;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
import com.raygroupintl.struct.Filter;

public abstract class AdditiveDataAggregator<T, U> {
	Block<U> block;
	BlocksSupply<Block<U>> supply;
	
	public AdditiveDataAggregator(Block<U> block, BlocksSupply<Block<U>> supply) {
		this.block = block;
		this.supply = supply;
	}
	
	protected abstract T getNewDataInstance(Block<U> block);
	
	protected abstract void updateData(T targetData, Block<U> fanoutBlock);
	
	public T get(Filter<EntryId> filter) {
		FanoutBlocks<Block<U>> fanoutBlocks = this.block.getFanoutBlocks(this.supply, filter);
		List<Block<U>> blocks = fanoutBlocks.getBlocks();
		T result = this.getNewDataInstance(this.block);
		for (Block<U> b : blocks) {
			this.updateData(result, b);
		}
		return result;		
	}
}
