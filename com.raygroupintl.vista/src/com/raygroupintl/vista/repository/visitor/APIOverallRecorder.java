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

package com.raygroupintl.vista.repository.visitor;

import java.util.logging.Logger;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.MapBlocksSupply;
import com.raygroupintl.m.parsetree.visitor.APIRecorder;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.repository.VistaPackage;

public class APIOverallRecorder extends RepositoryVisitor {
	private final static Logger LOGGER = Logger.getLogger(APIOverallRecorder.class.getName());

	private int packageCount;
	private APIRecorder recorder;
	private MapBlocksSupply blocksMap = new MapBlocksSupply();
	
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		++this.packageCount;
		super.visitVistaPackage(routinePackage);
		LOGGER.info(String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + "\n");
	}

	@Override
	public void visitRoutine(Routine routine) {
		routine.accept(this.recorder);
		Blocks blocks = this.recorder.getBlocks();
		this.blocksMap.put(routine.getName(), blocks);
	}
	
	@Override
	protected void visitRoutinePackages(VistaPackages rps) {
		this.recorder = new APIRecorder();
		rps.acceptSubNodes(this);
	}
	
	public BlocksSupply getBlocks() {
		return this.blocksMap;
	}
}
