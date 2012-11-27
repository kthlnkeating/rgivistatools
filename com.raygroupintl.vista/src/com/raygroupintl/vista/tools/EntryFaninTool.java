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

package com.raygroupintl.vista.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.data.RecursiveDataHandler;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;

public class EntryFaninTool extends EntryInfoTool  {
	private static class PathsToEntry {
		private EntryId start;
		private SortedSet<EntryId> firstEntriesInPaths;
		
		public PathsToEntry(EntryId start) {
			this.start = start;
		}
		
		public boolean exist() {
			return this.firstEntriesInPaths != null;
		}
		
		public boolean add(EntryId firstEntryInPath) {
			if (this.firstEntriesInPaths == null) {
				this.firstEntriesInPaths = new TreeSet<EntryId>();
			}
			if (! this.firstEntriesInPaths.contains(firstEntryInPath)) {
				this.firstEntriesInPaths.add(firstEntryInPath);
				return true;
			}
			return false;
		}
		
		public SortedSet<EntryId> getAll() {
			return this.firstEntriesInPaths;
		}
	}
		
	public static class AllPaths implements ToolResult {
		public EntryId endPoint;
		Map<EntryId, SortedSet<EntryId>> pathStart = new TreeMap<EntryId, SortedSet<EntryId>>();
	
		public AllPaths(EntryId endPoint) {
			this.endPoint = endPoint;
		}

		public void add(PathsToEntry pte) {
			this.pathStart.put(pte.start, pte.getAll());
		}
		
		@Override
		public void write(Terminal t, TerminalFormatter tf) {
			t.writeEOL(" " + this.endPoint.toString2());
		}
	}
	
	private static class SettableBoolean implements RecursiveDataHandler<PathsToEntry> {
		private EntryId startNode;
		private EntryId endNode;
		
		public SettableBoolean(EntryId startNode) {
			this.startNode = startNode;			
		}
		
		public void set(EntryId endNode) {
			this.endNode = endNode;
		}
		
		@Override
		public PathsToEntry getLocalCopy()  {
			return new PathsToEntry(this.startNode);
		}
		
		@Override
		public int update(PathsToEntry target, PathsToEntry source, int sourceIndex) {
			int changeCount = 0;
			if (source.exist()) {
				EntryId sourceStart = source.start;
				if (target.add(sourceStart)) {
					++changeCount;
				}
			} 
			if (this.endNode != null) {
				if (target.add(this.endNode)) {
					++changeCount;
				}
			}
			return changeCount;
		}
	}
	
	public static class Recorder extends BlockRecorder<SettableBoolean> {
		private EntryId entryId;
		
		public Recorder(EntryId entryId) {
			this.entryId = entryId;
		}
	
		protected void updateFanout(EntryId fanout) {
			if (this.entryId.equals(fanout)) {
				SettableBoolean b = this.getCurrentBlockAttachedObject();
				b.set(entryId);
			}
		}
		
		@Override
		protected Block<SettableBoolean> getNewBlock(int index, EntryId entryId, Blocks<SettableBoolean> blocks, String[] params) {
			return new Block<SettableBoolean>(index, entryId, blocks, new SettableBoolean(entryId));
		}
	}
	
	private static class RecorderFactory implements BlockRecorderFactory<SettableBoolean> {
		private EntryId entryId;
		
		public RecorderFactory(EntryId entryId) {
			this.entryId = entryId;
		}
		
		@Override
		public BlockRecorder<SettableBoolean> getRecorder() {
			return new Recorder(this.entryId);
		}
	}
	
	public EntryFaninTool(CLIParams params) {
		super(params);
	}
	
	private class FaninAccumulator extends RepositoryVisitor {
		private DataStore<PathsToEntry> store = new DataStore<PathsToEntry>();					
		private BlocksSupply<SettableBoolean> blocksSupply;
		private RepositoryInfo ri;
		private EntryId endNode;
		
		public FaninAccumulator(RepositoryInfo ri, EntryId endNode) {
			this.ri = ri;
			this.endNode = endNode;
			this.blocksSupply = EntryFaninTool.this.getBlocksSupply(ri, new RecorderFactory(endNode));		
		}
		
		@Override
		protected void visitRoutine(Routine routine) {
			List<EntryId> routineEntryTags = routine.getEntryIdList();
			for (EntryId routineEntryTag : routineEntryTags) {
				Filter<EntryId> filter = EntryFaninTool.this.getFilter(this.ri, routineEntryTag);
				Block<SettableBoolean> b = blocksSupply.getBlock(routineEntryTag);
				RecursiveDataAggregator<PathsToEntry, SettableBoolean> ala = new RecursiveDataAggregator<PathsToEntry, SettableBoolean>(b, blocksSupply);
				ala.get(store, filter);				
			}
		}		

		@Override
		protected void visitVistaPackage(VistaPackage routinePackage) {
			MRALogger.logInfo(routinePackage.getPackageName() + "...writing");
			super.visitVistaPackage(routinePackage);
			MRALogger.logInfo("..done.\n");
		}

		public AllPaths getResult() {
			this.store = new DataStore<PathsToEntry>();
			VistaPackages vps = EntryFaninTool.this.getVistaPackages(this.ri);
			vps.accept(this);
			AllPaths result = new AllPaths(this.endNode);
			for (PathsToEntry p : this.store.values()) {
				result.add(p);
			}
			return result;
		}	
	}
	
	public List<ToolResult> getResult(RepositoryInfo ri, List<EntryId> entries) {
		List<ToolResult> resultList = new ArrayList<ToolResult>();
		for (EntryId entryId : entries) {
			FaninAccumulator fia = new FaninAccumulator(ri, entryId);
			ToolResult result = fia.getResult();
			resultList.add(result);
		}
		return resultList;
	}
}
