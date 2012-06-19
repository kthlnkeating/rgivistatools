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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.ElseCmd;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.EntryId;
import com.raygroupintl.m.parsetree.ForLoop;
import com.raygroupintl.m.parsetree.IfCmd;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.QuitCmd;
import com.raygroupintl.m.parsetree.Routine;

public class APIRecorder extends FanoutRecorder {
	private final static Logger LOGGER = Logger.getLogger(APIRecorder.class.getName());

	public static class IndexedFanout {
		private int index;
		private EntryId fanout;
		private boolean isGoto;
		private boolean isConditional;
		
		public IndexedFanout(int index, EntryId fanout, boolean isGoto, boolean isConditional) {
			this.index = index;
			this.fanout = fanout;
			this.isGoto = isGoto;
			this.isConditional = isConditional;
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
		private Blocks siblings;
				
		private int index;
		private boolean closed;
		
		public Block(int index, Blocks siblings) {
			this.index = index;
			this.siblings = siblings;
		}
		
		public void close() {
			this.closed = true;
		}
		
		public int getIndex() {
			return this.index;
		}
		
		public void addNewed(int index, Local local) {
			if (! this.closed) {
				this.newedLocals.put(local.getName().toString(), index);
			}
		}		
		
		public void addUsed(int index, Local local) {
			if (! this.closed) {
				this.usedLocals.put(local.getName().toString(), index);
			}
		}

		public void addFanout(int index, EntryId fanout, boolean isGoto, boolean isConditional) {
			if (! this.closed) {
				IndexedFanout ifo = new IndexedFanout(index, fanout, isGoto, isConditional);
				this.fanouts.add(ifo);
			}
		}	
		
		public boolean isNewed(Local local) {
			return this.newedLocals.containsKey(local.getName().toString());
		}
		
		public boolean isUsed(Local local) {
			return this.usedLocals.containsKey(local.getName().toString());
		}
		
		private void mergeUsed(Set<String> current, Set<String> source, int sourceIndex) {
			for (String name : source) {
				Integer index = this.newedLocals.get(name);
				if (index == null) {
					this.usedLocals.put(name, sourceIndex);
				} else if (index.intValue() > sourceIndex) {
					this.usedLocals.put(name, sourceIndex);
				}
			}
		}
		
		public Set<String> getUseds(Map<String, Blocks> overallMap) {
			Set<String> result = this.usedLocals.keySet();
			for (IndexedFanout ifout : this.fanouts) {
				EntryId fout = ifout.getFanout();
				String routineName = fout.getRoutineName();
				String tagName = fout.getTag();					
				if (tagName == null) {
					tagName = routineName;
				}
				if (routineName == null) {
					Block tagBlock = this.siblings.get(tagName);
					if (tagBlock == null) {
						LOGGER.log(Level.SEVERE, "Unable to find information about tag " + tagName);
						continue;
					}
					Set<String> blockUsed = tagBlock.getUseds(overallMap);
					this.mergeUsed(result, blockUsed, tagBlock.getIndex());
				} else {
					Blocks routineBlocks = overallMap.get(routineName);
					if (routineBlocks == null) {
						LOGGER.log(Level.SEVERE, "Unable to find information about routine " + routineName);
						continue;
					}
					Block tagBlock = routineBlocks.get(tagName);
					if (tagBlock == null) {
						LOGGER.log(Level.SEVERE, "Unable to find information about tag " + fout.toString());
						continue;
					}
					Set<String> blockUsed = tagBlock.getUseds(overallMap);
					this.mergeUsed(result, blockUsed, tagBlock.getIndex());
				}				 
			}
			return result;
		}
	}
	
	public static class Blocks extends HashMap<String, Block> {		
	}
	
	private Blocks currentBlocks;
	
	private Block currentBlock;
	private Block firstInCurrentBlocks;
	
	private String lastRoutineName;
	private int index;
	
	private int underCondition;
	private int underFor;
	
	private void reset() {
		this.currentBlocks = null;
		this.lastRoutineName = null;
		this.currentBlock = null;
		this.firstInCurrentBlocks = null;
		this.index = 0;		
		this.underCondition = 0;
		this.underFor = 0;		
	}
	
	private void addUsed(Local local) {
		if (! this.currentBlock.isUsed(local)) {		
			++index;
			this.currentBlock.addUsed(index, local);
		}	
	}
	
	protected void assignLocal(Local local) {
		this.addUsed(local);
	}
	
	protected void killLocal(Local local) {		
		this.addUsed(local);
	}
	
	protected void newLocal(Local local) {
		this.addUsed(local);
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
		
	protected void visitQuit(QuitCmd quitCmd) {
		quitCmd.acceptSubNodes(this);
		boolean quitConditional = (quitCmd.getPostCondition() != null);
		if ((! quitConditional) && (this.underFor < 1) && (this.underCondition < 1)) {
			this.currentBlock.close();			
		}
	}

	protected void visitForLoop(ForLoop forLoop) {
		++this.underFor;
		super.visitForLoop(forLoop);
		--this.underFor;
	}
	
	protected void visitIf(IfCmd ifCmd) {
		++this.underCondition;
		super.visitIf(ifCmd);
		--this.underCondition;
	}
	
	protected void visitElse(ElseCmd elseCmd) {
		++this.underCondition;
		super.visitElse(elseCmd);
		--this.underCondition;
	}

	protected void visitEntry(Entry entry) {
		Block lastBlock = this.currentBlock;
		++this.index;
		this.currentBlock = new Block(this.index, this.currentBlocks);
		if (lastBlock == null) {
			this.firstInCurrentBlocks = this.currentBlock;
			String tag = entry.getKey();
			if ((tag != null) && (! tag.isEmpty())) {
				this.currentBlocks.put(tag, this.currentBlock);
			}
		} else {
			String tag = entry.getKey();
			if ((tag == null) || tag.isEmpty()) {
				tag = ":" + String.valueOf(this.index);
			}
			this.currentBlocks.put(tag, this.currentBlock);
		}
		super.visitEntry(entry);
	}
			
	protected void visitDoBlock(DoBlock doBlock) {
		Blocks lastBlocks = this.currentBlocks;
		Block lastBlock = this.currentBlock;
		this.currentBlock = null;
		this.currentBlocks = new Blocks();
		super.visitDoBlock(doBlock);
		this.currentBlocks = lastBlocks;
	}
	
	protected void visitRoutine(Routine routine) {
		this.reset();
		this.currentBlocks = new Blocks();
		this.lastRoutineName = routine.getName();
		super.visitRoutine(routine);
	}
}
