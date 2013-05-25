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

package com.raygroupintl.m.tool.entry.localassignment;

import java.util.Set;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeLocations;

public class LocalAssignmentRecorder extends BlockRecorder<Fanout, CodeLocations> {
	private Set<String> localNames;
	
	public LocalAssignmentRecorder(Set<String> localNames)  {
		this.localNames = localNames;
	}
	
	@Override
	protected CodeLocations getNewBlockData(EntryId entryId, String[] params) {
		CodeLocations ecls = new CodeLocations(entryId);
		return ecls;
	}

	protected void setLocal(Local local, Node rhs) {
		String name = local.getName().toString();
		if (this.localNames.contains(name)) {
			CodeLocations ecls = this.getCurrentBlockData();
			CodeLocation cl = this.getCodeLocation();
			ecls.add(cl);
		}
	}
	
	@Override
	protected Fanout getFanout(EntryId id, FanoutType type) {
		return new Fanout(id, type);
	}
}