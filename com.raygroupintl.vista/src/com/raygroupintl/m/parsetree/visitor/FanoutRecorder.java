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
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.EnvironmentFanoutRoutine;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.Fanout;
import com.raygroupintl.m.parsetree.FanoutLabel;
import com.raygroupintl.m.parsetree.FanoutRoutine;
import com.raygroupintl.m.parsetree.IndirectFanoutLabel;
import com.raygroupintl.m.parsetree.IndirectFanoutRoutine;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.RoutinePackage;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Filter;

public class FanoutRecorder extends LocationMarker {
	private static class LastInfo {
		public IndirectFanoutLabel lastIndirectFanoutLabel;
		public FanoutLabel lastFanoutLabel;
		public IndirectFanoutRoutine lastIndirectFanoutRoutine;
		public FanoutRoutine lastFanoutRoutine;
		public EnvironmentFanoutRoutine lastEnvironmentFanoutRoutine;
		
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
	}
		
	private Map<LineLocation, List<Fanout>> fanouts;
	private LastInfo lastInfo = new LastInfo();
	private Filter<Fanout> filter;

	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		this.filter = routinePackage.getPackageFanoutFilter();
		super.visitRoutinePackage(routinePackage);
	}
	
	protected void visitIndirectFanoutLabel(IndirectFanoutLabel label) {
		this.lastInfo.lastIndirectFanoutLabel = label;
		super.visitIndirectFanoutLabel(label);
	}
		
	protected void visitFanoutLabel(FanoutLabel label) {
		this.lastInfo.lastFanoutLabel = label;
		super.visitFanoutLabel(label);
	}
		
	protected void visitIndirectFanoutRoutine(IndirectFanoutRoutine routine) {
		this.lastInfo.lastIndirectFanoutRoutine = routine;
		super.visitIndirectFanoutRoutine(routine);
	}
		
	protected void visitEnvironmentFanoutRoutine(EnvironmentFanoutRoutine routine) {
		this.lastInfo.lastEnvironmentFanoutRoutine = routine;
		super.visitEnvironmentFanoutRoutine(routine);
	}
		
	protected void visitFanoutRoutine(FanoutRoutine routine) {
		this.lastInfo.lastFanoutRoutine = routine;
		super.visitFanoutRoutine(routine);
	}
	
	private void updateFanout() {
		Fanout fanout = this.lastInfo.getFanout();
		if (fanout != null) {
			if (this.filter != null) {
				if (! this.filter.isValid(fanout)) return;
			}			
			LineLocation location = this.getLastLocation();
			List<Fanout> fanoutsOnLocation = this.fanouts.get(location);
			if (fanoutsOnLocation == null) {
				fanoutsOnLocation = new ArrayList<Fanout>();
				this.fanouts.put(location, fanoutsOnLocation);
			}
			fanoutsOnLocation.add(fanout);
		}		
	}
		
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.lastInfo.reset();
		super.visitAtomicDo(atomicDo);
		this.updateFanout();
	}
	
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		this.lastInfo.reset();
		super.visitAtomicGoto(atomicGoto);
		this.updateFanout();
	}
	
	protected void visitExtrinsic(Extrinsic extrinsic) {
		LastInfo current = this.lastInfo;
		this.lastInfo = new LastInfo();
		super.visitExtrinsic(extrinsic);
		this.updateFanout();
		this.lastInfo = current;
	}
	
	public void visitRoutine(Routine routine) {
		this.fanouts = new HashMap<LineLocation, List<Fanout>>();
		super.visitRoutine(routine);
	}

	public Map<LineLocation, List<Fanout>> getRoutineFanouts() {
		return this.fanouts;
	}
	
	public void setFilter(Filter<Fanout> filter) {
		this.filter = filter;
	}
	
	public Map<LineLocation, List<Fanout>> getFanouts(Routine routine) {
		routine.accept(this);
		return this.fanouts;
	}		
}