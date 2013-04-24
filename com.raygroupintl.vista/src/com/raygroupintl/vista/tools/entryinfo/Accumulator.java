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
import com.raygroupintl.struct.ConstFilterFactory;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.struct.PassFilter;

public abstract class Accumulator<T, B> {
	protected BlocksSupply<Block<B>> blocksSupply;
	private FilterFactory<EntryId, EntryId> filterFactory;
	
	protected Accumulator(BlocksSupply<Block<B>> blocksSupply) {
		this(blocksSupply, new ConstFilterFactory<EntryId, EntryId>(new PassFilter<EntryId>()));
	}

	protected Accumulator(BlocksSupply<Block<B>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
		this.blocksSupply = blocksSupply;
		this.filterFactory = filterFactory;
	}

	public void setFilterFactory(FilterFactory<EntryId, EntryId> filterFactory) {
		this.filterFactory = filterFactory;
	}
	
	protected abstract T getResult(Block<B> block, Filter<EntryId> filter);
	
	protected abstract T getEmptyBlockResult(EntryId entryId);
	
	protected T getResult(EntryId entryId) {
		Block<B> b = this.blocksSupply.getBlock(entryId);
		if (b != null) {
			Filter<EntryId> filter = this.filterFactory.getFilter(entryId);
			return this.getResult(b, filter);
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
