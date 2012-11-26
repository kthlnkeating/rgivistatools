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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.BasicCodeInfo;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.CodeInfo;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.parsetree.visitor.EntryCodeInfoRecorder;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class EntryCodeInfoTool extends EntryInfoTool {	
	public static class EntryCodeInfo implements ToolResult {
		public EntryId entryId;
		public String[] formals;
		public Set<String> assumedLocals;
		public BasicCodeInfo otherCodeInfo;
	
		public EntryCodeInfo(EntryId entryId, String[] formals, Set<String> assumedLocals, BasicCodeInfo otherCodeInfo) {
			this.entryId = entryId;
			this.formals = formals;
			this.assumedLocals = assumedLocals;
			this.otherCodeInfo = otherCodeInfo;
		}
		
		@Override
		public void write(Terminal t, TerminalFormatter tf) {
			t.writeEOL(" " + this.entryId.toString2());		
			if ((this.formals == null) && (this.assumedLocals == null) && (this.otherCodeInfo == null)) {
				t.writeEOL("  ERROR: Invalid entry point");
				return;
			} else {
				t.writeFormatted("FORMAL", this.formals, tf);
				
				List<String> assumedLocalsSorted = new ArrayList<String>(this.assumedLocals);
				Collections.sort(assumedLocalsSorted);			
				t.writeFormatted("ASSUMED", assumedLocalsSorted, tf);
				
				t.writeFormatted("GLBS", this.otherCodeInfo.getGlobals(), tf);
				t.writeFormatted("READ" , this.otherCodeInfo.getReadCount(), tf);
				t.writeFormatted("WRITE", this.otherCodeInfo.getWriteCount(), tf);
				t.writeFormatted("EXEC", this.otherCodeInfo.getExecuteCount(), tf);
				t.writeFormatted("IND", this.otherCodeInfo.getIndirectionCount(), tf);
				t.writeFormatted("FMGLBS", this.otherCodeInfo.getFilemanGlobals(), tf);
				t.writeFormatted("FMCALLS", this.otherCodeInfo.getFilemanCalls(), tf);
				t.writeEOL();
			}
		}
	}
	
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

	public EntryCodeInfoTool(CLIParams params) {
		super(params);
	}
	
	public ToolResult getResult(DataStore<Set<String>> store, BlocksSupply<CodeInfo> blocksSupply, EntryId entryId, Filter<EntryId> filter) {
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
		BlocksSupply<CodeInfo> blocksSupply = this.getBlocksSupply(ri, new EntryCodeInfoRecorderFactory(ri));
		List<ToolResult> resultList = new ArrayList<ToolResult>();
		for (EntryId entryId : entries) {
			Filter<EntryId> filter = this.getFilter(ri, entryId);
			ToolResult result = this.getResult(store, blocksSupply, entryId, filter);
			resultList.add(result);
		}
		return resultList;
	}
}
