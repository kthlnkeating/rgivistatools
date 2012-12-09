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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

public class EntryFanoutAccumulator {
	private Map<EntryId, EntryFanouts> fanouts = new TreeMap<EntryId, EntryFanouts>();
	
	public void addRoutine(Routine routine) {
		EntryFanoutBR recorder = new EntryFanoutBR();
		RoutineFanouts rawFanouts = recorder.getResults(routine);
		String routineName = routine.getName();
		Set<String> tags = rawFanouts.getRoutineEntryTags();
		for (String tag : tags) {
			EntryId eid = new EntryId(routineName, tag);
			EntryFanouts efouts = new EntryFanouts(eid); 
			Set<EntryId> values = rawFanouts.getFanouts(tag);
			for (EntryId v : values) {
				efouts.add(v);
			}
			this.fanouts.put(eid, efouts);
		}
	}

	public void addRoutines(Collection<Routine> routines) {
		for (Routine routine : routines) {
			this.addRoutine(routine);
		}
	}

	public Map<EntryId, EntryFanouts> getResult() {
		return this.fanouts;
	}

	public void write(Terminal t, TerminalFormatter tf) {
		Set<EntryId> eids = this.fanouts.keySet();
		for (EntryId eid : eids) {
			EntryFanouts rfos = this.fanouts.get(eid);
			rfos.write(t, tf);			
			t.writeEOL();
		}		
	}	
}
