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

package com.raygroupintl.vista.tools.entryfanin;

import java.util.Collection;
import java.util.List;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.ConstFilterFactory;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.tools.fnds.AccumulatorOnRoutine;

public class EntryFaninAccumulator implements AccumulatorOnRoutine  {
	private EntryId entryUnderTest;
	private BlocksSupply<Block<FaninMark>> blocksSupply;
	private DataStore<PathPieceToEntry> store = new DataStore<PathPieceToEntry>();					
	private FilterFactory<EntryId, EntryId> filterFactory = new ConstFilterFactory<EntryId, EntryId>(new PassFilter<EntryId>());
	
	public EntryFaninAccumulator(EntryId entryUnderTest, BlocksSupply<Block<FaninMark>> blocksSupply) {
		this.entryUnderTest = entryUnderTest;
		this.blocksSupply = blocksSupply;
	}
	
	@Override
	public void setFilterFactory(FilterFactory<EntryId, EntryId> filterFactory) {
		this.filterFactory = filterFactory;
	}
	
	@Override
	public void addRoutine(Routine routine) {
		List<EntryId> routineEntryTags = routine.getEntryIdList();
		for (EntryId routineEntryTag : routineEntryTags) {
			Filter<EntryId> filter = this.filterFactory.getFilter(routineEntryTag);
			Block<FaninMark> b = this.blocksSupply.getBlock(routineEntryTag);
			if (b != null) {
				EntryFaninsAggregator ag = new EntryFaninsAggregator(b, this.blocksSupply);
				ag.get(store, filter);
			}
		}		
	}
	
	public void addRoutines(Collection<Routine> routines) {
		for (Routine routine : routines) {
			this.addRoutine(routine);
		}
	}
		
	@Override
	public EntryFanins getResult() {
		EntryFanins result = new EntryFanins(this.entryUnderTest);
		for (PathPieceToEntry p : this.store.values()) {
			if (p != null) {
				result.add(p);
			}
		}
		return result;
	}
}
