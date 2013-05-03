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

package com.raygroupintl.m.tool.entry.quitype;

import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.entry.AdditiveDataAggregator;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryTool;
import com.raygroupintl.struct.Filter;

public class QuitTypeTool extends MEntryTool<QuitType, Fanout, QTBlockData> {
	private class QTRecorderFactory implements BlockRecorderFactory<Fanout, QTBlockData> {
		@Override
		public QTRecorder getRecorder() {
			return new QTRecorder();
		}
	}

	private class QTDataAggregator extends AdditiveDataAggregator<QuitType, Fanout, QTBlockData> {
		public QTDataAggregator(Block<Fanout, QTBlockData> block, BlocksSupply<Fanout, QTBlockData> supply) {
			super(block, supply);
		}

		protected QuitType getNewDataInstance(Block<Fanout, QTBlockData> block) {
			return new QuitType();
		}
		
		protected void updateData(QuitType targetData, Block<Fanout, QTBlockData> fanoutBlock) {
		}		
	}
	
	public QuitTypeTool(CommonToolParams params) {
		super(params);
	}
	
	@Override
	protected QTRecorderFactory getBlockRecorderFactory() {
		return new QTRecorderFactory();
	}

	@Override
	protected QuitType getResult(Block<Fanout, QTBlockData> block, Filter<Fanout> filter, Set<EntryId> missingEntryIds) {
		QTDataAggregator bcia = new QTDataAggregator(block, this.blocksSupply);
		QuitType apiData = bcia.get(filter, missingEntryIds);
		return apiData;
	}
}