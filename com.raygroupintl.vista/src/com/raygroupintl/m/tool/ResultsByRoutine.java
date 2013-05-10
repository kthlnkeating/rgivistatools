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

package com.raygroupintl.m.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.EntryId;

public abstract class ResultsByRoutine<T, U extends Collection<T>> {
	private Map<String, ResultsByLabel<T, U>> map = new HashMap<String, ResultsByLabel<T, U>>();

	public void put(String routineName, ResultsByLabel<T, U> results) {
		this.map.put(routineName, results);
	}
	
	public Set<String> getRoutineNames() {
		return this.map.keySet();
	}
	
	public ResultsByLabel<T, U> getResults(String routineName) {
		return this.map.get(routineName);
	}
	
	public abstract ResultsByLabel<T, U> getNewResultsInstance();
	
	public U getResultsAddingWhenNone(String routineName, String label) {
		ResultsByLabel<T, U> results = this.map.get(routineName);
		if (results == null) {
			results = this.getNewResultsInstance();
			this.put(routineName, results);
		}
		return results.getResultsAddingWhenNone(label);		
	}
	
	public void addResult(EntryId entryId, T result){
		String routineName = entryId.getRoutineName();
		String label = entryId.getLabelOrDefault();
		U results = this.getResultsAddingWhenNone(routineName, label);
		results.add(result);
	}
	
	public List<EntryId> getEmptyEntries() {
		List<EntryId> emptyEntries = new ArrayList<EntryId>();
		Set<String> routineNames = this.map.keySet();
		for (String routineName : routineNames) {
			ResultsByLabel<T, U> results = this.map.get(routineName);
			List<String> emptyLabels = results.getLabelsWithEmptyResults();
			for (String emptyLabel : emptyLabels) {
				emptyEntries.add(new EntryId(routineName, emptyLabel));
			}
		}
		return emptyEntries;
	}
}
