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

package com.raygroupintl.m.parsetree.visitor;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.DeadCmds;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.InnerEntryList;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.EntryObject;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlockData;
import com.raygroupintl.struct.HierarchicalMap;

public abstract class BlockRecorder<F extends EntryObject, T extends BlockData<F>> extends FanoutRecorder {
	private HierarchicalMap<String, Block<F, T>> currentBlocks;
	
	private T currentBlockData;
	private String currentRoutineName;
	
	private int index;
	
	private Set<Integer> innerEntryListHash = new HashSet<Integer>();

	protected T getCurrentBlockData() {
		return this.currentBlockData;
	}
	
	protected String getCurrentRoutineName() {
		return this.currentRoutineName;
	}
	
	protected int getIndex() {
		return this.index;
	}
	
	public void reset() {
		this.currentBlocks = null;
		this.currentBlockData = null;
		this.currentRoutineName = null;
		this.index = 0;		
		this.innerEntryListHash = new HashSet<Integer>();
	}
	
	protected abstract void postUpdateFanout(EntryId fanout, CallArgument[] callArguments);
	
	@Override
	protected void updateFanout(EntryId fanoutId) {
		if (fanoutId != null) {
			CallArgument[] callArguments = this.getLastArguments();
			F fo = this.getFanout(fanoutId);
			this.currentBlockData.addFanout(fo);	
			this.postUpdateFanout(fanoutId, callArguments);
			++this.index;
		} 
	}

	protected abstract T getNewBlockData(EntryId entryId, String[] params);
	
	protected abstract F getFanout(EntryId id); 
 	
	@Override
	protected void visitDeadCmds(DeadCmds deadCmds) {
		if (deadCmds.getLevel() > 0) {
			super.visitDeadCmds(deadCmds);
		}
	}
	
	@Override
	protected void visitEntry(Entry entry) {
		String tag = entry.getName();
		EntryId entryId = entry.getFullEntryId();		
		this.currentBlockData = this.getNewBlockData(entryId, entry.getParameters());
		Block<F, T> b = new Block<F, T>(this.currentBlocks, this.currentBlockData);
		this.currentBlocks.put(tag, b);
		super.visitEntry(entry);
		if (entry.getContinuationEntry() != null) {
			Entry ce = entry.getContinuationEntry();
			String ceTag = ce.getName();
			EntryId defaultGoto = new EntryId(null, ceTag);
			F fo = this.getFanout(defaultGoto);
			this.currentBlockData.addFanout(fo);
			++this.index;
		}
	}
			
	@Override
	protected void visitInnerEntryList(InnerEntryList entryList) {
		int id = System.identityHashCode(entryList);
		if (! this.innerEntryListHash.contains(id)) {
			this.innerEntryListHash.add(id);
			HierarchicalMap<String, Block<F, T>> lastBlocks = this.currentBlocks;
			T lastBlock = this.currentBlockData;
			this.currentBlockData = null;
			this.currentBlocks = new HierarchicalMap<String, Block<F, T>>(lastBlocks);
			super.visitInnerEntryList(entryList);
			String tag = entryList.getName();
			this.currentBlocks = lastBlocks;
			this.currentBlockData = lastBlock;
			if ((lastBlock != null)) {
				EntryId defaultDo = new EntryId(null, tag);
				F fo = this.getFanout(defaultDo);
				lastBlock.addFanout(fo);
				++this.index;
			}
		}
	}
		
	public HierarchicalMap<String, Block<F, T>> getBlocks() {
		return this.currentBlocks;
	}
	
	@Override
	protected void visitRoutine(Routine routine) {
		this.reset();
		this.currentBlocks = new HierarchicalMap<String, Block<F, T>>();
		this.currentRoutineName = routine.getName();
		super.visitRoutine(routine);
	}
}
