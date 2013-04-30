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

package com.raygroupintl.m.tool.entry;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.IndexedFanout;

public class BlockData {
	private EntryId entryId;
	private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();
	
	public BlockData(EntryId entryId) {
		this.entryId = entryId;
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public void addFanout(int index, EntryId fanout) {
		IndexedFanout ifo = new IndexedFanout(index, fanout);			
		this.fanouts.add(ifo);
	}	
	
	public List<IndexedFanout> getFanouts() {
		return this.fanouts;
	}
	
	public List<EntryId> getFanoutIds() {
		List<EntryId> result = new ArrayList<EntryId>();
		for (IndexedFanout ifo : this.fanouts) {
			EntryId fo = ifo.getEntryId();
			result.add(fo);
		}
		return result;
	}
}
