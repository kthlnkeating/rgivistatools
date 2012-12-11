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

package com.raygroupintl.vista.tools.entryfanout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;

public class EntryFanoutBR extends BlockRecorder<Void> {
	@Override
	protected void postUpdateFanout(EntryId fanout, CallArgument[] callArguments) {		
	}
	
	@Override
	protected Block<Void> getNewBlock(int index, EntryId entryId, Blocks<Block<Void>> blocks, String[] params) {
		return new Block<Void>(index, entryId, blocks, null);
	}
	
	private Set<EntryId> getBlockFanouts(Block<Void> b, String routineName, Blocks<Block<Void>> siblings, Set<String> parentAlready) {
		Set<EntryId> r = new HashSet<EntryId>();
		List<EntryId> fs = b.getFanouts();
		for (EntryId f : fs) {
			String rname = f.getRoutineName();
			if ((rname == null) || rname.equals(routineName)) {
				String label = f.getLabelOrDefault();
				Block<Void> cb = b.getChildBlock(label);
				if (cb != null) {
					Set<String> already = new HashSet<String>();
					already.add(label);
					Set<EntryId> cr = this.getBlockFanouts(cb, routineName, cb.getParent(), already); 
					r.addAll(cr);				
					continue;
				} 
				if ((siblings != null)) {
					Block<Void> sb = siblings.get2(label);
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
		Blocks<Block<Void>> bs = super.getBlocks();
		RoutineFanouts result = new RoutineFanouts();
		Set<String> tags = bs.getTags();
		String routineName = routine.getName();
		for (String tag : tags) {
			Block<Void> b = bs.get(tag);
			Set<EntryId> bfouts = this.getBlockFanouts(b, routineName, null, null);
			result.put(tag, bfouts);
		}		
		return result;		
	}
}
