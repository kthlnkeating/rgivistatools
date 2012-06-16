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

package com.raygroupintl.m.parsetree.visitor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Filter;

public class FanInRecorder extends FanoutRecorder {
	private Set<Fanout> fanins = new HashSet<>();
	
	public FanInRecorder(Filter<Fanout> filter) {
		super(filter);
	}

	protected void visitRoutine(Routine routine) {
		super.visitRoutine(routine);
		Map<LineLocation, List<Fanout>> fanouts = this.getRoutineFanouts();
		if (fanouts != null) {
			for (List<Fanout> fs : fanouts.values()) {
				for (Fanout f : fs) {
					this.fanins.add(f);
				}		
			}
		}
	}
	
	public void reset() {
		this.fanins = new HashSet<>();
	}
	
	public Set<Fanout> getFanIns() {
		return this.fanins;
	}
}
