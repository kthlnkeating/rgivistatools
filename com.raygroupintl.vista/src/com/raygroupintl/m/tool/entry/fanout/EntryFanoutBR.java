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

package com.raygroupintl.m.tool.entry.fanout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlockData;
import com.raygroupintl.struct.HierarchicalMap;

public class EntryFanoutBR extends BlockRecorder<IndexedFanout, BlockData<IndexedFanout>> {
	@Override
	protected BlockData<IndexedFanout> getNewBlockData(EntryId entryId, String[] params) {
		return new BlockData<IndexedFanout>(entryId);
	}
	
	private Set<EntryId> getBlockFanouts(Block<IndexedFanout, BlockData<IndexedFanout>> b, String routineName, HierarchicalMap<String, Block<IndexedFanout, BlockData<IndexedFanout>>> siblings, Set<String> parentAlready) {
		Set<EntryId> r = new HashSet<EntryId>();
		List<EntryId> fs = b.getData().getFanoutIds();
		for (EntryId f : fs) {
			String rname = f.getRoutineName();
			if ((rname == null) || rname.equals(routineName)) {
				String label = f.getLabelOrDefault();
				Block<IndexedFanout, BlockData<IndexedFanout>> cb = b.getCallableBlocks().getChildBlock(label);
				if (cb != null) {
					Set<String> already = new HashSet<String>();
					already.add(label);
					Set<EntryId> cr = this.getBlockFanouts(cb, routineName, cb.getCallableBlocks(), already); 
					r.addAll(cr);				
					continue;
				} 
				if ((siblings != null)) {
					Block<IndexedFanout, BlockData<IndexedFanout>> sb = siblings.get(label);
					if ((sb != null)  && (! parentAlready.contains(label))) {
						parentAlready.add(label);
						Set<EntryId> sr = this.getBlockFanouts(sb, routineName, siblings, parentAlready); 
						r.addAll(sr);				
						continue;
					}
				}
			}
			r.add(f);
		}		
		return r;
	}
 	
	public RoutineFanouts getResults(Routine routine) {
		routine.accept(this);
		HierarchicalMap<String, Block<IndexedFanout, BlockData<IndexedFanout>>> bs = super.getBlocks();
		RoutineFanouts result = new RoutineFanouts();
		Set<String> tags = bs.keySet();
		String routineName = routine.getName();
		for (String tag : tags) {
			Block<IndexedFanout, BlockData<IndexedFanout>> b = bs.getThruHierarchy(tag);
			Set<EntryId> bfouts = this.getBlockFanouts(b, routineName, null, null);
			result.put(tag, bfouts);
		}		
		return result;		
	}
	
	@Override
	protected IndexedFanout getFanout(EntryId id, FanoutType type) {
		int index = this.getIndex();
		return new IndexedFanout(index, id, type);
	}
}
