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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class SerializedBlocksSupply implements BlocksSupply {
	private String inputPath;
	private HashMap<String, Blocks> blocks = new HashMap<String, Blocks>();
	private RepositoryInfo repositoryInfo;
	
	public SerializedBlocksSupply(String inputPath, RepositoryInfo ri) {
		this.inputPath = inputPath;
		this.repositoryInfo = ri;
	}
	
	@Override
	public Blocks getBlocks(String routineName) {
		if (! this.blocks.containsKey(routineName)) {			
			Blocks result = null;
			Path path = Paths.get(this.inputPath, routineName + ".ser");
			Routine routine = Routine.readSerialized(path.toString());
			if (routine != null) {
				APIRecorder recorder = new APIRecorder(this.repositoryInfo);
				routine.accept(recorder);
				result = recorder.getBlocks();
			}
			this.blocks.put(routineName, result);
			return result;
		}
		return this.blocks.get(routineName);
	}	
}
