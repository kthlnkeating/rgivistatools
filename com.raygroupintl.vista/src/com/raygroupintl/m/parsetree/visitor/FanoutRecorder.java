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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.EnvironmentFanoutRoutine;
import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.FanoutLabel;
import com.raygroupintl.m.parsetree.FanoutRoutine;
import com.raygroupintl.m.parsetree.IndirectFanoutLabel;
import com.raygroupintl.m.parsetree.IndirectFanoutRoutine;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.LineLocation;

public class FanoutRecorder extends LineLocationMarker {
	private Map<LineLocation, List<Fanout>> fanouts;
	
	private IndirectFanoutLabel lastIndirectFanoutLabel;
	private FanoutLabel lastFanoutLabel;
	private IndirectFanoutRoutine lastIndirectFanoutRoutine;
	private FanoutRoutine lastFanoutRoutine;
	private EnvironmentFanoutRoutine lastEnvironmentFanoutRoutine;
	
	private void reset() {
		this.lastIndirectFanoutLabel = null;
		this.lastFanoutLabel = null;
		this.lastIndirectFanoutRoutine = null;
		this.lastEnvironmentFanoutRoutine = null;
		this.lastFanoutRoutine = null;		
	}
	
	private Fanout getFanout() {
		if ((this.lastFanoutLabel == null) && (this.lastFanoutRoutine == null)) {
			return null;
		}
		if ((this.lastIndirectFanoutLabel != null) || (this.lastIndirectFanoutRoutine != null) || this.lastEnvironmentFanoutRoutine != null) {
			return null;
		}
		String label = (this.lastFanoutLabel == null) ? null : this.lastFanoutLabel.getValue();
		String routine = (this.lastFanoutRoutine == null) ? null : this.lastFanoutRoutine.getName();
	
		return new Fanout(routine, label);
	}

	protected void visitIndirectFanoutLabel(IndirectFanoutLabel label) {
		this.lastIndirectFanoutLabel = label;
		super.visitIndirectFanoutLabel(label);
	}
		
	protected void visitFanoutLabel(FanoutLabel label) {
		this.lastFanoutLabel = label;
		super.visitFanoutLabel(label);
	}
		
	protected void visitIndirectFanoutRoutine(IndirectFanoutRoutine routine) {
		this.lastIndirectFanoutRoutine = routine;
		super.visitIndirectFanoutRoutine(routine);
	}
		
	protected void visitEnvironmentFanoutRoutine(EnvironmentFanoutRoutine routine) {
		this.lastEnvironmentFanoutRoutine = routine;
		super.visitEnvironmentFanoutRoutine(routine);
	}
		
	protected void visitFanoutRoutine(FanoutRoutine routine) {
		this.lastFanoutRoutine = routine;
		super.visitFanoutRoutine(routine);
	}
		
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.reset();
		super.visitAtomicDo(atomicDo);
		LineLocation location = this.getLastLocation();
		Fanout fanout = this.getFanout();
		if (fanout != null) {
			List<Fanout> fanoutsOnLocation = this.fanouts.get(location);
			if (fanoutsOnLocation == null) {
				fanoutsOnLocation = new ArrayList<Fanout>();
				this.fanouts.put(location, fanoutsOnLocation);
			}
			fanoutsOnLocation.add(fanout);
		}
	}
	
	public Map<LineLocation, List<Fanout>> getFanouts(Routine routine) {
		this.fanouts = new HashMap<LineLocation, List<Fanout>>();
		this.visitRoutine(routine);
		return this.fanouts;
	}
		
}