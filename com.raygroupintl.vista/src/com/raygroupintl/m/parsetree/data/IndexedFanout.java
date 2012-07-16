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

package com.raygroupintl.m.parsetree.data;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.struct.Indexed;


public class IndexedFanout {
	private int index;
	private EntryId fanout;
	private List<Indexed<String>> byRefs;
		
	public IndexedFanout(int index, EntryId fanout) {
		this.index = index;
		this.fanout = fanout;
	}

	public void setByRefs(CallArgument[] callArguments) {
		this.byRefs = null;
		if (callArguments != null) {
			int count = 0;
			for (int i=0; i<callArguments.length; ++i) {
				CallArgument ca = callArguments[i];
				if ((ca != null) && (ca.getType() == CallArgumentType.LOCAL_BY_REF)) ++count;
			}
			if (count > 0) {
				this.byRefs = new ArrayList<Indexed<String>>(count);
				for (int i=0; i<callArguments.length; ++i) {
					CallArgument ca = callArguments[i];
					if ((ca != null) && (ca.getType() == CallArgumentType.LOCAL_BY_REF)) {
						Indexed<String> is = new Indexed<String>(ca.getValue(), i);
						this.byRefs.add(is);
					}
				}
			}
		}
	}
	
	public int getIndex() {
		return this.index;
	}
		
	public EntryId getFanout() {
		return this.fanout;
	}
	
	public List<Indexed<String>> getByRefs() {
		return this.byRefs;
	}
}
	
