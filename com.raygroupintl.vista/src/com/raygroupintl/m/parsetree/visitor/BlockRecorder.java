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

import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.DeadCmds;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.InnerEntryList;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.EntryObject;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlockData;
import com.raygroupintl.struct.HierarchicalMap;

public abstract class BlockRecorder<F extends EntryObject, T extends BlockData<F>> extends LocationMarker {
	private HierarchicalMap<String, Block<F, T>> currentBlocks;
	private T currentBlockData;
	private String currentRoutineName;
	private int index;
	private InnerEntryList lastInnerEntryList;

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
		this.lastInnerEntryList = null;
	}
	
	protected abstract void postUpdateFanout(EntryId fanout, CallArgument[] callArguments);
	
	protected void updateFanout(EntryId fanoutId, CallArgument[] callArguments) {
		if (fanoutId != null) {
			F fo = this.getFanout(fanoutId);
			this.currentBlockData.addFanout(fo);	
			this.postUpdateFanout(fanoutId, callArguments);
			++this.index;
		} 
	}

	@Override
	protected void visitAtomicDo(AtomicDo atomicDo) {
		super.visitAtomicDo(atomicDo);		
		this.updateFanout(atomicDo.getFanoutId(), atomicDo.getCallArguments());
	}
	
	@Override
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		super.visitAtomicGoto(atomicGoto);
		this.updateFanout(atomicGoto.getFanoutId(), null);
	}
	
	@Override
	protected void visitExtrinsic(Extrinsic extrinsic) {
		super.visitExtrinsic(extrinsic);
		this.updateFanout(extrinsic.getFanoutId(), extrinsic.getCallArguments());
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
		if (entryList != this.lastInnerEntryList) {
			this.lastInnerEntryList = entryList;
			HierarchicalMap<String, Block<F, T>> lastBlocks = this.currentBlocks;
			
			T lastBlock = this.currentBlockData;
			if ((lastBlock != null)) {
				String tag = entryList.getName();
				EntryId defaultDo = new EntryId(null, tag);
				F fo = this.getFanout(defaultDo);
				lastBlock.addFanout(fo);
				++this.index;
			}
						
			this.currentBlockData = null;
			this.currentBlocks = new HierarchicalMap<String, Block<F, T>>(lastBlocks);
			super.visitInnerEntryList(entryList);
			this.currentBlocks = lastBlocks;
			this.currentBlockData = lastBlock;
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
