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

package com.raygroupintl.vista.repository.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.data.RecursiveDataHandler;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.tools.MRALogger;

public class EntryFaninRecorder extends RepositoryVisitor  {
	private static class PathsToEntry {
		private EntryId start;
		private SortedSet<EntryId> firstEntriesInPaths;
		
		public PathsToEntry(EntryId start) {
			this.start = start;
		}
		
		public boolean exist() {
			return this.firstEntriesInPaths != null;
		}
		
		public void add(EntryId firstEntryInPath) {
			if (this.firstEntriesInPaths == null) {
				this.firstEntriesInPaths = new TreeSet<EntryId>();
			}
			this.firstEntriesInPaths.add(firstEntryInPath);
		}
		
		public SortedSet<EntryId> getAll() {
			return this.firstEntriesInPaths;
		}
	}
		
	private static class PTEDataStore extends DataStore<PathsToEntry> {
		private Map<EntryId, PathsToEntry> path = new HashMap<EntryId, PathsToEntry>();
		
		@Override
		public <U> PathsToEntry put(Block<U> block, Map<Integer, PathsToEntry> datas) {
			EntryId eid = block.getEntryId();
			PathsToEntry entries = super.put(block, datas);
			if (entries != null) {
				this.path.put(eid, entries);
			}
			return entries;
		}
		
		public Set<EntryId> getFanins() {
			return this.path.keySet();
		}
	}

	private static class PrivateRecursiveDataHandler implements RecursiveDataHandler<PathsToEntry> {
		private EntryId start;
		
		public PrivateRecursiveDataHandler(EntryId start) {
			this.start = start;
		}
		
		@Override
		public PathsToEntry getLocalCopy() {
			return new PathsToEntry(this.start);
		}
		
		@Override
		public int update(PathsToEntry target, PathsToEntry source, int sourceIndex) {
			if (source.exist()) {
				EntryId sourceStart = source.start;
				target.add(sourceStart);
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	private BlocksSupply<PrivateRecursiveDataHandler> blocksSupply;
	private Map<String, String> replacementRoutines;
	private SourcedFanoutFilter filter = new BasicSourcedFanoutFilter(new PassFilter<EntryId>());

	PTEDataStore store = new PTEDataStore();

	public EntryFaninRecorder(BlocksSupply<PrivateRecursiveDataHandler> blocksSupply, Map<String, String> replacementRoutines) {
		this.blocksSupply = blocksSupply;
		this.replacementRoutines = replacementRoutines;
	}
	
	private void update(EntryId entryId) {
		String routineName = entryId.getRoutineName();
		Blocks<PrivateRecursiveDataHandler> rbs = this.blocksSupply.getBlocks(routineName);
		if (rbs == null) {
			MRALogger.logError("Invalid entry point: " + entryId.toString2());
			return;
		} 
		String label = entryId.getLabelOrDefault();
		Block<PrivateRecursiveDataHandler> lb = rbs.get(label);
		if (lb == null) {
			MRALogger.logError("Invalid entry point: " + entryId.toString2());
			return;
		}
		RecursiveDataAggregator<PathsToEntry, PrivateRecursiveDataHandler> ala = new RecursiveDataAggregator<PathsToEntry, PrivateRecursiveDataHandler>(lb, this.blocksSupply);
		ala.get(this.store, this.filter, this.replacementRoutines);
	}
		
	@Override
	public void visitRoutine(Routine routine) {
		List<EntryId> entryIds = routine.getEntryIdList();
		for (EntryId entryId : entryIds) {
			this.update(entryId);
		}
	}
	
	public Set<EntryId> getFanins() {
		return this.store.getFanins();
	}
}
