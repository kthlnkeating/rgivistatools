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

public class BlockData<T> {
	private EntryId entryId;
	private List<IndexedFanout> fanouts = new ArrayList<IndexedFanout>();
	private T attachedObject;
	
	public BlockData(EntryId entryId, T attachedObject) {
		this.entryId = entryId;
		this.attachedObject = attachedObject;
	}

	public EntryId getEntryId() {
		return this.entryId;
	}

	public void addFanout(int index, EntryId fanout) {
		IndexedFanout ifo = new IndexedFanout(index, fanout);			
		this.fanouts.add(ifo);
	}	
	
	public List<IndexedFanout> getIndexedFanouts() {
		return this.fanouts;
	}
	
	public List<EntryId> getFanouts() {
		List<EntryId> result = new ArrayList<EntryId>();
		for (IndexedFanout ifo : this.fanouts) {
			EntryId fo = ifo.getFanout();
			result.add(fo);
		}
		return result;
	}
	
	public T getAttachedObject() {
		return this.attachedObject;
	}
}
