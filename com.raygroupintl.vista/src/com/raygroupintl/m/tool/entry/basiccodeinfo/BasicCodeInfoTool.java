//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.raygroupintl.m.tool.entry.basiccodeinfo;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.entry.AdditiveDataAggregator;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class BasicCodeInfoTool extends MEntryTool<BasicCodeInfoTR, Fanout, CodeInfo> {
	private class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<Fanout, CodeInfo> {
		@Override
		public EntryCodeInfoRecorder getRecorder() {
			return new EntryCodeInfoRecorder(BasicCodeInfoTool.this.repositoryInfo);
		}
	}

	private class BCITDataAggregator extends AdditiveDataAggregator<BasicCodeInfo, Fanout, CodeInfo> {
		public BCITDataAggregator(Block<Fanout, CodeInfo> block, BlocksSupply<Fanout, CodeInfo> supply) {
			super(block, supply);
		}

		protected BasicCodeInfo getNewDataInstance(Block<Fanout, CodeInfo> block) {
			return new BasicCodeInfo();
		}
		
		protected void updateData(BasicCodeInfo targetData, CodeInfo fanoutData) {
			targetData.mergeGlobals(fanoutData.getGlobals());
			targetData.mergeFilemanGlobals(fanoutData.getFilemanGlobals());
			targetData.mergeFilemanCalls(fanoutData.getFilemanCalls());
			
			targetData.incrementIndirectionCount(fanoutData.getIndirectionCount());
			targetData.incrementReadCount(fanoutData.getReadCount());
			targetData.incrementWriteCount(fanoutData.getWriteCount());
			targetData.incrementExecuteCount(fanoutData.getExecuteCount());		
		}		
	}
	
	private RepositoryInfo repositoryInfo;
	
	public BasicCodeInfoTool(BasicCodeInfoToolParams params) {
		super(params);
		this.repositoryInfo = params.getRepositoryInfo();
	}
	
	@Override
	protected BlockRecorderFactory<Fanout, CodeInfo> getBlockRecorderFactory() {
		return new EntryCodeInfoRecorderFactory();
	}

	@Override
	protected BasicCodeInfoTR getResult(Block<Fanout, CodeInfo> block, Filter<Fanout> filter, Set<EntryId> missingEntryIds) {
		BCITDataAggregator bcia = new BCITDataAggregator(block, blocksSupply);
		BasicCodeInfo apiData = bcia.get(filter, missingEntryIds);
		return new BasicCodeInfoTR(block.getData().getFormals(), apiData);
	}
}
