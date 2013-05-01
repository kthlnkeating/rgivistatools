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

package com.raygroupintl.m.tool.entry;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.EntryObject;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.data.FanoutBlocks;
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
public class Block<F extends Fanout, T extends BlockData<F>> implements EntryObject {
	private HierarchicalMap<String, Block<F, T>> callables;
	private T blockData;
	
	public Block(HierarchicalMap<String, Block<F, T>> callables, T blockData) {
		this.callables = callables;
		this.blockData = blockData;
	}
	
	@Override
	public EntryId getEntryId() {
		return this.blockData.getEntryId();
	}
		
	public HierarchicalMap<String, Block<F, T>> getCallableBlocks() {
		return this.callables;
	}
	
	public T getData() {
		return this.blockData;
	}
	
	private void update(FanoutBlocks<F, T> fanoutBlocks, BlocksSupply<Block<F, T>> blocksSupply, Filter<Fanout> filter, Set<EntryId> missing) {
		for (F ifout : this.blockData.getFanouts()) {
			if ((filter != null) && (! filter.isValid(ifout))) continue;
			EntryId fout = ifout.getEntryId();
			String routineName = fout.getRoutineName();
			String tagName = fout.getTag();					
			if (tagName == null) {
				tagName = routineName;
			}			
			if (routineName == null) {
				Block<F, T> tagBlock = this.callables.getChildBlock(tagName);
				if (tagBlock == null) {
					tagBlock = this.callables.getThruHierarchy(tagName);
				}
				if (tagBlock == null) {
					EntryId missingId = new EntryId( this.getEntryId().getRoutineName(), tagName);
					missing.add(missingId);
					continue;
				}
				fanoutBlocks.add(this, tagBlock, ifout);
			} else {
				HierarchicalMap<String, Block<F, T>> routineBlocks = blocksSupply.getBlocks(routineName);
				if (routineBlocks == null) {
					missing.add(fout);
					continue;
				}
				Block<F, T> tagBlock = routineBlocks.getThruHierarchy(tagName);
				if (tagBlock == null) {
					missing.add(fout);
					continue;
				}
				fanoutBlocks.add(this, tagBlock, ifout);
			}				 
		}
	}
	
	private void updateFanoutBlocks(FanoutBlocks<F, T> fanoutBlocks, BlocksSupply<Block<F, T>> blocksSupply, Filter<Fanout> filter, Set<EntryId> missing) {
		int index = 0;	
		while (index < fanoutBlocks.getSize()) {
			Block<F, T> b = fanoutBlocks.getBlock(index);
			b.update(fanoutBlocks, blocksSupply, filter, missing);
			++index;
		}		
	}
	
	public FanoutBlocks<F, T> getFanoutBlocks(BlocksSupply<Block<F, T>> blocksSupply, ObjectIdContainer blockIdContainer, Filter<Fanout> filter, Set<EntryId> missing) {
		FanoutBlocks<F, T> fanoutBlocks = new FanoutBlocks<F, T>(this, blockIdContainer);
		this.updateFanoutBlocks(fanoutBlocks, blocksSupply, filter, missing);
		return fanoutBlocks;
	}
	
	public FanoutBlocks<F, T> getFanoutBlocks(BlocksSupply<Block<F, T>> blocksSupply, Filter<Fanout> filter, Set<EntryId> missing) {
		FanoutBlocks<F, T> fanoutBlocks = new FanoutBlocks<F, T>(this, null);
		this.updateFanoutBlocks(fanoutBlocks, blocksSupply, filter, missing);
		return fanoutBlocks;
	}
	
	public boolean isInternal() {
		return this.getCallableBlocks().getParent() != null;
	}
}
