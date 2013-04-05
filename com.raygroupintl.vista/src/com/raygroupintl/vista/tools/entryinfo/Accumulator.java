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

import java.util.Collection;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.ConstFilterFactory;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.tools.fnds.ToolResult;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public abstract class Accumulator<T extends ToolResult, B> {
	protected BlocksSupply<Block<B>> blocksSupply;
	private FilterFactory<EntryId, EntryId> filterFactory;
	private ToolResultCollection<T> results;
	
	protected Accumulator(BlocksSupply<Block<B>> blocksSupply, ToolResultCollection<T> results) {
		this(blocksSupply, new ConstFilterFactory<EntryId, EntryId>(new PassFilter<EntryId>()), results);
	}

	protected Accumulator(BlocksSupply<Block<B>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory, ToolResultCollection<T> results) {
		this.blocksSupply = blocksSupply;
		this.filterFactory = filterFactory;
		this.results = results;
	}

	public void setFilterFactory(FilterFactory<EntryId, EntryId> filterFactory) {
		this.filterFactory = filterFactory;
	}
	
	protected abstract T getResult(Block<B> block, Filter<EntryId> filter);
	
	protected abstract T getEmptyBlockResult(EntryId entryId);
	
	public T getResult(EntryId entryId) {
		Block<B> b = this.blocksSupply.getBlock(entryId);
		if (b != null) {
			Filter<EntryId> filter = this.filterFactory.getFilter(entryId);
			return this.getResult(b, filter);
		} else {
			return this.getEmptyBlockResult(entryId);
		}		
	}

	public void addEntry(EntryId entryId) {
		T e = this.getResult(entryId);
		this.results.add(e);
	}
	
	public void addRoutine(Routine routine) {
		List<EntryId> routineEntryTags = routine.getEntryIdList();
		for (EntryId routineEntryTag : routineEntryTags) {
			this.addEntry(routineEntryTag);
		}		
	}
	
	public void addRoutines(Collection<Routine> routines) {
		for (Routine routine : routines) {
			this.addRoutine(routine);
		}
	}
	
	public ToolResultCollection<T> getResult() {
		return this.results;
	}
	
	public T getLastResult() {
		return this.results.getLast();
	}
}
