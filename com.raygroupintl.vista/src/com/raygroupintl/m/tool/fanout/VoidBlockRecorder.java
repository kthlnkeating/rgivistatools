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

package com.raygroupintl.m.tool.fanout;

import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.struct.HierarchicalMap;

public class VoidBlockRecorder extends BlockRecorder<Void> {
	@Override
	protected void postUpdateFanout(EntryId fanout, CallArgument[] callArguments) {		
	}
	
	@Override
	protected Block<Void> getNewBlock(EntryId entryId, HierarchicalMap<String, Block<Void>> blocks, String[] params) {
		return new Block<Void>(entryId, blocks, null);
	}
}
