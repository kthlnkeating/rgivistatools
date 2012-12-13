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

package com.raygroupintl.vista.tools.entryfanin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.vista.tools.entryfanout.RoutineFanouts;
import com.raygroupintl.vista.tools.fnds.ToolUtilities;

public class BlocksInSerialFanouts extends BlocksSupply<Block<FaninMark>> {
	private EntryId entryUnderTest;
	private String inputPath;
	private HashMap<String, Blocks<Block<FaninMark>>> blocks = new HashMap<String, Blocks<Block<FaninMark>>>();
	
	public BlocksInSerialFanouts(EntryId entryUnderTest, String inputPath, Map<String, String> replacementRoutines) {
		super(replacementRoutines);
		this.entryUnderTest = entryUnderTest;
		this.inputPath = inputPath;
	}
	
	private Blocks<Block<FaninMark>> getBlocks(String routineName, RoutineFanouts fanouts) {
		if (fanouts != null) {
			Blocks<Block<FaninMark>> result = new Blocks<Block<FaninMark>>();
			Set<String> entryTags = fanouts.getRoutineEntryTags();
			int index = 0;
			EntryId eidUnderTest = this.entryUnderTest;
			if (routineName.equals(this.entryUnderTest.getRoutineName())) {
				eidUnderTest = new EntryId(null, this.entryUnderTest.getLabelOrDefault());
			}
			for (String entryTag : entryTags) {
				EntryId beid = new EntryId(routineName, entryTag);
				Set<EntryId> entryFanouts = fanouts.getFanouts(entryTag);
				FaninMark fim = new FaninMark(beid);
				if (entryFanouts.contains(eidUnderTest)) {
					fim.set(this.entryUnderTest);
				}
				Block<FaninMark> b = new Block<FaninMark>(index, beid, result, fim);
				int foindex = 0;
				for (EntryId eid : entryFanouts) {
					b.addFanout(foindex, eid);
					++foindex;
				}
				result.put(entryTag, b);
				++index;
			}
			return result;
		}
		return null;		
	}
	
	@Override
	public Blocks<Block<FaninMark>> getBlocks(String routineName) {
		if (! this.blocks.containsKey(routineName)) {			
			RoutineFanouts fanouts = (RoutineFanouts) ToolUtilities.readSerializedRoutineObject(this.inputPath, routineName, ".fo");
			Blocks<Block<FaninMark>> result = this.getBlocks(routineName, fanouts);
			this.blocks.put(routineName, result);
			return result;
		}
		return this.blocks.get(routineName);
	}	
}
