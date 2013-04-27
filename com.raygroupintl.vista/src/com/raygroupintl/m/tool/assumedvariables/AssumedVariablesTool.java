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

package com.raygroupintl.m.tool.assumedvariables;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.MEntryTool;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;

public class AssumedVariablesTool extends MEntryTool<AssumedVariables, CodeInfo> {
	private static class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<CodeInfo> {
		private RepositoryInfo repositoryInfo;
		
		public EntryCodeInfoRecorderFactory(RepositoryInfo repositoryInfo) {
			this.repositoryInfo = repositoryInfo;
		}
		
		@Override
		public BlockRecorder<CodeInfo> getRecorder() {
			return new EntryCodeInfoRecorder(this.repositoryInfo);
		}
	}

	private DataStore<Set<String>> store = new DataStore<Set<String>>();					
	private AssumedVariablesToolParams params;
	private RepositoryInfo repositoryInfo;
	
	public AssumedVariablesTool(AssumedVariablesToolParams params) {
		super(params);
		this.params = params;
		this.repositoryInfo = params.getRepositoryInfo();
	}
	
	@Override
	protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory() {
		return new EntryCodeInfoRecorderFactory(this.repositoryInfo);
	}

	@Override
	public AssumedVariables getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(block, blocksSupply);
		Set<String> assumedVariables = ala.get(this.store, filter);
		if (assumedVariables != null) {
			assumedVariables.removeAll(this.params.getExpected());
		}
		return new AssumedVariables(assumedVariables);	
	}
	
	@Override
	protected AssumedVariables getEmptyBlockResult(EntryId entryId) {
		return new AssumedVariables(null);		
	}
}
