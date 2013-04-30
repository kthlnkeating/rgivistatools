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

import java.util.HashMap;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryObject;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.struct.HierarchicalMap;

public class AccumulatingBlocksSupply<F extends EntryObject, T extends BlockData<F>> extends BlocksSupply<Block<F, T>> {
	private ParseTreeSupply parseTreeSupply;
	private HashMap<String, HierarchicalMap<String, Block<F, T>>> blocks = new HashMap<String, HierarchicalMap<String, Block<F, T>>>();
	private BlockRecorderFactory<F, T> blockRecorder;
	
	public AccumulatingBlocksSupply(ParseTreeSupply parseTreeSupply, BlockRecorderFactory<F, T> brf) {
		this.parseTreeSupply = parseTreeSupply;
		this.blockRecorder = brf;
	}
	
	@Override
	public HierarchicalMap<String, Block<F, T>> getBlocks(String routineName) {
		if (! this.blocks.containsKey(routineName)) {			
			HierarchicalMap<String, Block<F, T>> result = null;
			Routine routine = this.parseTreeSupply.getParseTree(routineName);
			if (routine != null) {
				BlockRecorder<F, T> recorder = this.blockRecorder.getRecorder();
				routine.accept(recorder);
				result = recorder.getBlocks();
			}
			this.blocks.put(routineName, result);
			return result;
		}
		return this.blocks.get(routineName);
	}	
}
