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

package com.raygroupintl.m.tool.basiccodeinfo;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.AdditiveDataAggregator;
import com.raygroupintl.m.tool.MEntryTool;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class BasicCodeInfoTool extends MEntryTool<BasicCodeInfoTR, CodeInfo> {
	private class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<CodeInfo> {
		@Override
		public BlockRecorder<CodeInfo> getRecorder() {
			return new EntryCodeInfoRecorder(BasicCodeInfoTool.this.repositoryInfo);
		}
	}

	private class BCITDataAggregator extends AdditiveDataAggregator<BasicCodeInfo, CodeInfo> {
		public BCITDataAggregator(Block<CodeInfo> block, BlocksSupply<Block<CodeInfo>> supply) {
			super(block, supply);
		}

		protected BasicCodeInfo getNewDataInstance(Block<CodeInfo> block) {
			return new BasicCodeInfo();
		}
		
		protected void updateData(BasicCodeInfo targetData, Block<CodeInfo> fanoutBlock) {
			CodeInfo updateSource = fanoutBlock.getAttachedObject();
			targetData.mergeGlobals(updateSource.getGlobals());
			targetData.mergeFilemanGlobals(updateSource.getFilemanGlobals());
			targetData.mergeFilemanCalls(updateSource.getFilemanCalls());
			
			targetData.incrementIndirectionCount(updateSource.getIndirectionCount());
			targetData.incrementReadCount(updateSource.getReadCount());
			targetData.incrementWriteCount(updateSource.getWriteCount());
			targetData.incrementExecuteCount(updateSource.getExecuteCount());		
		}		
	}
	
	private RepositoryInfo repositoryInfo;
	
	public BasicCodeInfoTool(BasicCodeInfoToolParams params) {
		super(params);
		this.repositoryInfo = params.getRepositoryInfo();
	}
	
	@Override
	protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory() {
		return new EntryCodeInfoRecorderFactory();
	}

	@Override
	protected BasicCodeInfoTR getResult(Block<CodeInfo> block, Filter<EntryId> filter) {
		BCITDataAggregator bcia = new BCITDataAggregator(block, blocksSupply);
		Set<EntryId> missing = new HashSet<EntryId>();
		BasicCodeInfo apiData = bcia.get(filter, missing);
		return new BasicCodeInfoTR(block.getAttachedObject().getFormals(), apiData);
	}
	
	@Override
	protected BasicCodeInfoTR getEmptyBlockResult(EntryId entryId) {
		return new BasicCodeInfoTR(null, null);		
	}
}
