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

package com.raygroupintl.m.parsetree.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.HierarchicalMap;
import com.raygroupintl.struct.ObjectIdContainer;

/***
 * Block represents a piece of a custom partition of an M Routine. Each block
 * in this partition is closely related but not identical to an M execution 
 * block and is more granular.  
 * <p>
 * Every routine tag and the first line of a set of higher level M routine 
 * lines marks the starting point of a block.  An unconditional QUIT or GOTO
 * command, start of an other block of the same or lower level, or end
 * of the routine marks the end of a block. A block can contain other blocks
 * of higher level in which case the higher level block is simply considered
 * a fanout.  Explicit M DO and GOTO commands are also fanouts.  A new block 
 * that starts with a routine tag is implicitly considered to be a fanout
 * for the previous block if the previous block does not end with an 
 * unconditional QUIT or GOTO command.
 * <p> 
 * In addition to the standard information, Block can contain custom 
 * information of type T for various analysis needs.
 * 
 * @param <T> The type of additional analysis information.
 * @see com.raygroupintl.m.parsetree.visitor.BlockRecorder
 *           
 */
public class Block<T> {
	private EntryId entryId;
	private HierarchicalMap<String, Block<T>> callables;
	private List<Block<T>> children;
	private T attachedObject;
	
	private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();
	
	public Block(EntryId entryId, HierarchicalMap<String, Block<T>> callables, T attachedObject) {
		this.entryId = entryId;
		this.callables = callables;
		this.attachedObject = attachedObject;
	}
	
	public EntryId getEntryId() {
		return this.entryId;
	}
		
	public void addFanout(int index, EntryId fanout) {
		IndexedFanout ifo = new IndexedFanout(index, fanout);			
		this.fanouts.add(ifo);
	}	
	
	public void addChild(Block<T> child) {
		if (this.children == null) {
			this.children = new ArrayList<Block<T>>(2);
		}
		this.children.add(child);
	}
	
	public List<EntryId> getFanouts() {
		List<EntryId> result = new ArrayList<EntryId>();
		for (IndexedFanout ifo : this.fanouts) {
			EntryId fo = ifo.getFanout();
			result.add(fo);
		}
		return result;
	}
	
	public Block<T> getChildBlock(String tag) {
		if (this.children != null) {	
			for (Block<T> child : this.children) {
				String childTag = child.getEntryId().getLabelOrDefault();
				if (tag.equals(childTag)) {				
					return child;
				}
			}
		}
		return null;
	}
	
	public HierarchicalMap<String, Block<T>> getCallableBlocks() {
		return this.callables;
	}
	
	private void update(FanoutBlocks<Block<T>> fanoutBlocks, BlocksSupply<Block<T>> blocksSupply, Filter<EntryId> filter, Set<EntryId> missing) {
		for (IndexedFanout ifout : this.fanouts) {
			EntryId fout = ifout.getFanout();
			if ((filter != null) && (! filter.isValid(fout))) continue;
			String routineName = fout.getRoutineName();
			String tagName = fout.getTag();					
			if (tagName == null) {
				tagName = routineName;
			}			
			if (routineName == null) {
				Block<T> tagBlock = this.getChildBlock(tagName);
				if (tagBlock == null) {
					tagBlock = this.callables.getThruHierarchy(tagName);
				}
				if (tagBlock == null) {
					EntryId missingId = new EntryId( this.entryId.getRoutineName(), tagName);
					missing.add(missingId);
					continue;
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex());
			} else {
				HierarchicalMap<String, Block<T>> routineBlocks = blocksSupply.getBlocks(routineName);
				if (routineBlocks == null) {
					missing.add(fout);
					continue;
				}
				Block<T> tagBlock = routineBlocks.getThruHierarchy(tagName);
				if (tagBlock == null) {
					missing.add(fout);
					continue;
				}
				fanoutBlocks.add(this, tagBlock, ifout.getIndex());
			}				 
		}
	}
	
	private void updateFanoutBlocks(FanoutBlocks<Block<T>> fanoutBlocks, BlocksSupply<Block<T>> blocksSupply, Filter<EntryId> filter, Set<EntryId> missing) {
		int index = 0;	
		while (index < fanoutBlocks.getSize()) {
			Block<T> b = fanoutBlocks.getBlock(index);
			b.update(fanoutBlocks, blocksSupply, filter, missing);
			++index;
		}		
	}
	
	public FanoutBlocks<Block<T>> getFanoutBlocks(BlocksSupply<Block<T>> blocksSupply, ObjectIdContainer blockIdContainer, Filter<EntryId> filter, Set<EntryId> missing) {
		FanoutBlocks<Block<T>> fanoutBlocks = new FanoutBlocks<Block<T>>(this, blockIdContainer);
		this.updateFanoutBlocks(fanoutBlocks, blocksSupply, filter, missing);
		return fanoutBlocks;
	}
	
	public FanoutBlocks<Block<T>> getFanoutBlocks(BlocksSupply<Block<T>> blocksSupply, Filter<EntryId> filter, Set<EntryId> missing) {
		FanoutBlocks<Block<T>> fanoutBlocks = new FanoutBlocks<Block<T>>(this, null);
		this.updateFanoutBlocks(fanoutBlocks, blocksSupply, filter, missing);
		return fanoutBlocks;
	}
	
	public T getAttachedObject() {
		return this.attachedObject;
	}
	
	public boolean isInternal() {
		return this.getCallableBlocks().getParent() != null;
	}
}
