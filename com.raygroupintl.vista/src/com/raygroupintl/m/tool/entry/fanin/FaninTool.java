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

package com.raygroupintl.m.tool.entry.fanin;

import java.util.Collection;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.PassFilter;

public class FaninTool  {
	private BlocksSupply<Block<IndexedFanout, FaninMark>> blocksSupply;
	private DataStore<PathPieceToEntry> store = new DataStore<PathPieceToEntry>();					
	private Filter<Fanout> filter = new PassFilter<Fanout>();
	private boolean filterInternalBlocks;
	
	public FaninTool(BlocksSupply<Block<IndexedFanout, FaninMark>> blocksSupply, boolean filterInternalBlocks) {
		this.blocksSupply = blocksSupply;
		this.filterInternalBlocks = filterInternalBlocks;
	}
	
	public void setFilter(Filter<Fanout> filter) {
		this.filter= filter;
	}
	
	public void addRoutine(Routine routine) {
		List<EntryId> routineEntryTags = routine.getEntryIdList();
		for (EntryId routineEntryTag : routineEntryTags) {
			Block<IndexedFanout, FaninMark> b = this.blocksSupply.getBlock(routineEntryTag);
			if (b != null) {
				EntryFaninsAggregator ag = new EntryFaninsAggregator(b, this.blocksSupply, this.filterInternalBlocks);
				ag.get(store, this.filter);
			}
		}		
	}
	
	public void addRoutines(Collection<Routine> routines) {
		for (Routine routine : routines) {
			this.addRoutine(routine);
		}
	}
		
	public EntryFanins getResult() {
		EntryFanins result = new EntryFanins();
		for (PathPieceToEntry p : this.store.values()) {
			if (p != null) {
				result.add(p);
			}
		}
		return result;
	}
}
