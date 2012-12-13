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

package com.raygroupintl.vista.tools;

import java.util.List;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.CodeInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.EntryCodeInfoAccumulator;
import com.raygroupintl.vista.tools.entrycodeinfo.EntryCodeInfoRecorderFactory;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryCodeInfoTool extends EntryInfoTool {	
	public EntryCodeInfoTool(CLIParams params) {
		super(params);
	}
	
	public ToolResult getResult(final RepositoryInfo ri, List<EntryId> entries) {
		BlocksSupply<Block<CodeInfo>> blocksSupply = this.getBlocksSupply(ri, new EntryCodeInfoRecorderFactory(ri));
		FilterFactory<EntryId, EntryId> filterFactory = new FilterFactory<EntryId, EntryId>() {
			@Override
			public Filter<EntryId> getFilter(EntryId parameter) {
				return EntryCodeInfoTool.this.getFilter(ri, parameter);
			}
		}; 
		EntryCodeInfoAccumulator a = new EntryCodeInfoAccumulator(blocksSupply);
		a.setFilterFactory(filterFactory);
		for (EntryId entryId : entries) {
			a.addEntry(entryId);
		}
		return a.getResult();
	}
}
