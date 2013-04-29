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
import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.struct.HierarchicalMap;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;

public class BlocksInMap<T extends BlockData> extends BlocksSupply<Block<T>> {
	private final static Logger LOGGER = Logger.getLogger(BlocksInMap.class.getName());

	private static class Factory<T extends BlockData> extends RepositoryVisitor {
		private int packageCount;
		private BlockRecorder<T> recorder;
		private BlocksInMap<T> blocksMap;
		
		public Factory(BlockRecorder<T> br) {
			this.recorder= br;
			this.blocksMap = new BlocksInMap<T>();
		}
		
		@Override
		protected void visitVistaPackage(VistaPackage routinePackage) {
			++this.packageCount;
			super.visitVistaPackage(routinePackage);
			LOGGER.info(String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + "\n");
		}

		@Override
		public void visitRoutine(Routine routine) {
			routine.accept(this.recorder);
			HierarchicalMap<String, Block<T>> blocks = this.recorder.getBlocks();
			this.blocksMap.put(routine.getName(), blocks);
		}
		
		@Override
		protected void visitRoutinePackages(VistaPackages rps) {
			this.recorder.reset();
			rps.acceptSubNodes(this);
		}
		
		public BlocksInMap<T> getBlocks() {
			return this.blocksMap;
		}
	}

	private Map<String, HierarchicalMap<String, Block<T>>> map = new HashMap<String, HierarchicalMap<String, Block<T>>>();
	
	public HierarchicalMap<String, Block<T>> put(String routineName,  HierarchicalMap<String, Block<T>> blocks) {
		return this.map.put(routineName, blocks);
	}
	
	@Override
	public HierarchicalMap<String, Block<T>> getBlocks(String routineName) {
		return this.map.get(routineName);
	}
		
	public static <T extends BlockData> BlocksInMap<T> getInstance(BlockRecorder<T> br, RepositoryInfo ri) {
		Factory<T> f = new Factory<T>(br);
		VistaPackages vps = new VistaPackages(ri.getAllPackages());
		vps.accept(f);
		return f.getBlocks();		
	}
	
	public static <T extends BlockData> BlocksInMap<T> getInstance(BlockRecorder<T> br, Routine[] routines) {
		BlocksInMap<T> result = new BlocksInMap<T>();
		for (Routine r : routines) {
			br.reset();
			r.accept(br);
			HierarchicalMap<String, Block<T>> blocks = br.getBlocks();
			result.put(r.getName(), blocks);
		}
		return result;
	}
}
