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

import java.util.HashMap;
import java.util.Map;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;

public class BlocksInSerialRoutine<T> extends BlocksSupply<Block<T>> {
	private String inputPath;
	private HashMap<String, Blocks<Block<T>>> blocks = new HashMap<String, Blocks<Block<T>>>();
	private BlockRecorderFactory<T> blockRecorder;
	
	public BlocksInSerialRoutine(String inputPath, BlockRecorderFactory<T> brf, Map<String, String> replacementRoutines) {
		super(replacementRoutines);
		this.inputPath = inputPath;
		this.blockRecorder = brf;
	}
	
	@Override
	public Blocks<Block<T>> getBlocks(String routineName) {
		if (! this.blocks.containsKey(routineName)) {			
			Blocks<Block<T>> result = null;
			Routine routine = Routine.readSerialized(this.inputPath, routineName);
			if (routine != null) {
				BlockRecorder<T> recorder = this.blockRecorder.getRecorder();
				routine.accept(recorder);
				result = recorder.getBlocks();
			}
			this.blocks.put(routineName, result);
			return result;
		}
		return this.blocks.get(routineName);
	}	
}
