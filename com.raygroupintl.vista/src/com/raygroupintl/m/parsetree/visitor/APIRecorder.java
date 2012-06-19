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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.EntryId;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.LineLocation;

public class APIRecorder extends FanoutRecorder {
	public static class IndexedFanout {
		private int index;
		private EntryId fanout;
		
		public IndexedFanout(int index, EntryId fanout) {
			this.index = index;
			this.fanout = fanout;
		}
		
		public int getIndex() {
			return this.index;
		}
		
		public EntryId getFanout() {
			return this.fanout;
		}
	}
		
	public static class Block {
		private Map<String, Integer> newedLocals = new HashMap<String, Integer>();
		private Map<String, Integer> usedLocals = new HashMap<String, Integer>();
		private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();

		public void addNewed(int index, Local local) {
			this.newedLocals.put(local.getName().toString(), index);
		}

		public void addUsed(int index, Local local) {
			this.usedLocals.put(local.getName().toString(), index);
		}

		public void addFanout(int index, EntryId fanout) {
			IndexedFanout ifo = new IndexedFanout(index, fanout);
			this.fanouts.add(ifo);
		}		
	}
	
	public static class Blocks extends HashMap<String, Block> {		
	}
	
	private Block currentBlock;
	private Blocks currentBlocks;
	private String lastRoutineName;
	private int index;	
	
	protected void assignLocal(Local local) {
		++index;
		this.currentBlock.addUsed(index, local);
	}
	
	protected void killLocal(Local local) {		
		++index;
		this.currentBlock.addUsed(index, local);
	}
	
	protected void newLocal(Local local) {
		++index;
		this.currentBlock.addNewed(index, local);
	}
	
	protected void visitLocal(Local local) {
		super.visitLocal(local);
		++index;
		this.currentBlock.addUsed(index, local);
	}

	protected void updateFanout(boolean isGoto, boolean conditional) {
		EntryId fanout = this.getLastFanout();
		if (fanout != null) {
			String entryTag = fanout.getTag();
			String routineName = fanout.getRoutineName();
			if ((routineName != null) && (! routineName.equals(this.lastRoutineName))) {
				++index;
				//this.lastEntryAPIHelper.addFanout(index, fanout);
			}
		}		
	}
		
	protected void visitEntry(Entry entry) {
		Block lastBlock = this.currentBlock;
		this.currentBlock = new Block();
		super.visitEntry(entry);
	}
			
	protected void visitDoBlock(DoBlock doBlock) {
		Blocks lastBlocks = this.currentBlocks;
		this.currentBlocks = new Blocks();
		super.visitDoBlock(doBlock);
		this.currentBlocks = lastBlocks;
	}
	
	protected void visitRoutine(Routine routine) {
		this.currentBlocks = new Blocks();
		this.lastRoutineName = routine.getName();
		super.visitRoutine(routine);
	}
}
