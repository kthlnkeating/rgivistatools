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

package com.raygroupintl.vista.tools.entryfanout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;

public class RoutineFanouts implements Serializable {
	private static final long serialVersionUID = 1L;

	private Map<String, Set<EntryId>> fanouts = new HashMap<String, Set<EntryId>>();
	
	public RoutineFanouts() {		
	}

	public Set<EntryId> put(String tag, Set<EntryId> fanouts) {
		return this.fanouts.put(tag, fanouts);
	}
	
	public Set<String> getRoutineEntryTags() {
		return this.fanouts.keySet();
	}
	
	public Set<EntryId> getFanouts(String tag) {
		return this.fanouts.get(tag);
	}
}
