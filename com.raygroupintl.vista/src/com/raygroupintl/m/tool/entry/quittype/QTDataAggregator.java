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

package com.raygroupintl.m.tool.entry.quittype;

import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.m.parsetree.data.FanoutWithLocation;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.RecursiveDataAggregator;

public class QTDataAggregator extends RecursiveDataAggregator<QuitType, FanoutWithLocation, QTBlockData> {
	public QTDataAggregator(Block<FanoutWithLocation, QTBlockData> block, BlocksSupply<FanoutWithLocation, QTBlockData> supply) {
		super(block, supply);
	}

	@Override
	protected QuitType getNewDataInstance(QTBlockData data) {
		QuitType result = new QuitType(data.getQuitType());
		List<FanoutWithLocation> fs = data.getFanouts();
		for (FanoutWithLocation f : fs) {
			EntryId id = f.getEntryId();
			FanoutType type = f.getType();
			CodeLocation location = f.getCodeLocation();
			switch (type) {
			case DO:
				result.addFanout(id, new CallType(CallTypeState.DO_UNVERIFIED, location));
				break;
			case EXTRINSIC:
				result.addFanout(id, new CallType(CallTypeState.EXTRINSIC_UNVERIFIED, location));
				break;
			case GOTO:
			case ASSUMED_GOTO:
				result.addFanout(id, new CallType(CallTypeState.GOTO, location));
				break;
			default:
				break;
			}
		}
		return result;
	}
	
	@Override
	protected int updateData(QTBlockData targetBlockData, QuitType targetData, QuitType sourceData, FanoutWithLocation property) {
		EntryId foutId = property.getEntryId();
		CallType ct = targetData.getFanout(foutId);
		CallTypeState cts = ct.getState();
		if (cts == CallTypeState.GOTO) {
			CodeLocation location = ct.getLocation();
			return targetData.markQuitFromGoto(sourceData, location);
		} else {
			return ct.updateFromFanout(sourceData);
		}
	}		
}
