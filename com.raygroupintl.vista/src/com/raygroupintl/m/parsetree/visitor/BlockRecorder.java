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
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.EntryList;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.HierarchicalMap;

public abstract class BlockRecorder<T> extends FanoutRecorder {
	private HierarchicalMap<String, Block<T>> currentBlocks;
	
	private Block<T> currentBlock;
	private String currentRoutineName;
	
	private int index;
	
	private Set<Integer> doBlockHash = new HashSet<Integer>();

	protected Block<T> getCurrentBlock() {
		return this.currentBlock;
	}
	
	protected String getCurrentRoutineName() {
		return this.currentRoutineName;
	}
	
	protected int incrementIndex() {
		++this.index;
		return this.index;
	}
	
	public void reset() {
		this.currentBlocks = null;
		this.currentBlock = null;
		this.currentRoutineName = null;
		this.index = 0;		
		this.doBlockHash = new HashSet<Integer>();
	}
	
	protected abstract void postUpdateFanout(EntryId fanout, CallArgument[] callArguments);
	
	@Override
	protected void updateFanout() {
		EntryId fanout = this.getLastFanout();
		if (fanout != null) {
			int i = this.incrementIndex();
			CallArgument[] callArguments = this.getLastArguments();
			this.currentBlock.addFanout(i, fanout);	
			this.postUpdateFanout(fanout, callArguments);
		} 
	}

	protected abstract Block<T> getNewBlock(int index, EntryId entryId, HierarchicalMap<String, Block<T>> blocks, String[] params);
 	
	@Override
	protected void visitDeadCmds(DeadCmds deadCmds) {
		if (deadCmds.getLevel() > 0) {
			super.visitDeadCmds(deadCmds);
		}
	}
	
	@Override
	protected void visitEntry(Entry entry) {
		++this.index;
		String tag = entry.getName();
		EntryId entryId = entry.getFullEntryId();		
		this.currentBlock = this.getNewBlock(this.index, entryId, this.currentBlocks, entry.getParameters());
		if ((tag != null) && (! tag.isEmpty())) {
			this.currentBlocks.put(tag, this.currentBlock);
		}
		++this.index;
		super.visitEntry(entry);
		if (entry.isClosed()) {
			this.currentBlock.close();
		} 
		if (entry.getContinuationEntry() != null) {
			Entry ce = entry.getContinuationEntry();
			String ceTag = ce.getName();
			EntryId defaultGoto = new EntryId(null, ceTag);
			++this.index;
			this.currentBlock.addFanout(this.index, defaultGoto);
			++this.index;
		}
	}
			
	@Override
	protected void visitEntryList(EntryList entryList) {
		super.visitEntryList(entryList);
	}

	@Override
	protected void visitDoBlock(DoBlock doBlock) {
		EntryList entryList = doBlock.getEntryList();
		if (entryList == null) {
			doBlock.acceptPostCondition(this);			
			return;
		}
		
		int id = doBlock.getUniqueId();
		if (! this.doBlockHash.contains(id)) {
			doBlockHash.add(id);
			doBlock.acceptPostCondition(this);
			HierarchicalMap<String, Block<T>> lastBlocks = this.currentBlocks;
			Block<T> lastBlock = this.currentBlock;
			this.currentBlock = null;
			this.currentBlocks = new HierarchicalMap<String, Block<T>>(lastBlocks);
			doBlock.acceptEntryList(this);
			String tag = entryList.getName();
			Block<T> firstBlock = this.currentBlocks.getThruHierarchy(tag);
			this.currentBlocks = lastBlocks;
			this.currentBlock = lastBlock;
			if ((lastBlock != null) && (! lastBlock.isClosed())) {
				EntryId defaultDo = new EntryId(null, tag);
				lastBlock.addFanout(this.index, defaultDo);
				lastBlock.addChild(firstBlock);
			}
		}
	}
	
	public HierarchicalMap<String, Block<T>> getBlocks() {
		return this.currentBlocks;
	}
	
	public T getCurrentBlockAttachedObject() {
		if ((this.currentBlock != null) && (! this.currentBlock.isClosed())) {			
			return this.currentBlock.getAttachedObject();
		} else {
			return null;
		}
	}
	
	@Override
	protected void visitRoutine(Routine routine) {
		this.reset();
		this.currentBlocks = new HierarchicalMap<String, Block<T>>();
		this.currentRoutineName = routine.getName();
		super.visitRoutine(routine);
	}
}
