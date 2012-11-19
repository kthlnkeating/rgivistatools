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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.CodeInfo;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.tools.MRALogger;

public class EntryFaninRecorder extends RepositoryVisitor  {
	private BlocksSupply<CodeInfo> blocksSupply;
	private Map<String, String> replacementRoutines;
	private SourcedFanoutFilter filter = new BasicSourcedFanoutFilter(new PassFilter<EntryId>());

	DataStore<Set<String>> store = new DataStore<Set<String>>();

	public EntryFaninRecorder(BlocksSupply<CodeInfo> blocksSupply, Map<String, String> replacementRoutines) {
		this.blocksSupply = blocksSupply;
		this.replacementRoutines = replacementRoutines;
	}
	
	private void update(EntryId entryId) {
		String routineName = entryId.getRoutineName();
		Blocks<CodeInfo> rbs = this.blocksSupply.getBlocks(routineName);
		if (rbs == null) {
			MRALogger.logError("Invalid entry point: " + entryId.toString2());
			return;
		} 
		String label = entryId.getLabelOrDefault();
		Block<CodeInfo> lb = rbs.get(label);
		if (lb == null) {
			MRALogger.logError("Invalid entry point: " + entryId.toString2());
			return;
		}
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(lb, this.blocksSupply);
		ala.get(this.store, this.filter, this.replacementRoutines);
	}
		
	@Override
	public void visitRoutine(Routine routine) {
		List<EntryId> entryIds = routine.getEntryIdList();
		for (EntryId entryId : entryIds) {
			this.update(entryId);
		}
	}
}
