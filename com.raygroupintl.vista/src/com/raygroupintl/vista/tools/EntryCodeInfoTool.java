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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.BasicCodeInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.CodeInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.EntryCodeInfo;
import com.raygroupintl.vista.tools.entrycodeinfo.EntryCodeInfoRecorderFactory;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryCodeInfoTool extends EntryInfoTool {	
	public EntryCodeInfoTool(CLIParams params) {
		super(params);
	}
	
	public ToolResult getResult(DataStore<Set<String>> store, BlocksSupply<Block<CodeInfo>> blocksSupply, EntryId entryId, Filter<EntryId> filter) {
		Block<CodeInfo> b = blocksSupply.getBlock(entryId);
		if (b != null) {
			RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(b, blocksSupply);
			Set<String> assumedLocals = ala.get(store, filter);
			AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(b, blocksSupply);
			BasicCodeInfo apiData = bcia.get(filter);
			return new EntryCodeInfo(entryId, b.getAttachedObject().getFormals(), assumedLocals, apiData);
		} else {
			return new EntryCodeInfo(entryId, null, null, null);
		}
	}
	
	public List<ToolResult> getResult(RepositoryInfo ri, List<EntryId> entries) {
		DataStore<Set<String>> store = new DataStore<Set<String>>();					
		BlocksSupply<Block<CodeInfo>> blocksSupply = this.getBlocksSupply(ri, new EntryCodeInfoRecorderFactory(ri));
		List<ToolResult> resultList = new ArrayList<ToolResult>();
		for (EntryId entryId : entries) {
			Filter<EntryId> filter = this.getFilter(ri, entryId);
			ToolResult result = this.getResult(store, blocksSupply, entryId, filter);
			resultList.add(result);
		}
		return resultList;
	}
}
