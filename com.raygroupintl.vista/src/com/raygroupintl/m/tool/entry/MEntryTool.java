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

package com.raygroupintl.m.tool.entry;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.struct.Filter;

public abstract class MEntryTool<T, F extends Fanout, B extends BlockData<F>> {
	protected BlocksSupply<Block<F, B>> blocksSupply;
	private Filter<Fanout> filter;
	
	protected MEntryTool(CommonToolParams params) {
		BlockRecorderFactory<F, B> f = this.getBlockRecorderFactory();
		this.blocksSupply = params.getBlocksSupply(f);
		this.filter = params.getFanoutFilter();
	}

	protected abstract BlockRecorderFactory<F, B> getBlockRecorderFactory();
	
	protected abstract T getResult(Block<F, B> block, Filter<Fanout> filter);
	
	protected abstract T getEmptyBlockResult(EntryId entryId);
	
	public T getResult(EntryId entryId) {
		Block<F, B> b = this.blocksSupply.getBlock(entryId);
		if (b != null) {
			return this.getResult(b, this.filter);
		} else {
			return this.getEmptyBlockResult(entryId);
		}		
	}

	public List<T> getResult(List<EntryId> entries) {
		List<T> result = new ArrayList<T>();
		for (EntryId entryId : entries) {
			T e = this.getResult(entryId);
			result.add(e);
		}		
		return result;
	}
}
