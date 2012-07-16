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

import com.raygroupintl.m.parsetree.ActualList;
import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.Do;
import com.raygroupintl.m.parsetree.EnvironmentFanoutRoutine;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.FanoutLabel;
import com.raygroupintl.m.parsetree.FanoutRoutine;
import com.raygroupintl.m.parsetree.Goto;
import com.raygroupintl.m.parsetree.IndirectFanoutLabel;
import com.raygroupintl.m.parsetree.IndirectFanoutRoutine;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Filter;

public class FanoutRecorder extends LocationMarker {
	private static class LastInfo {
		public IndirectFanoutLabel lastIndirectFanoutLabel;
		public FanoutLabel lastFanoutLabel;
		public IndirectFanoutRoutine lastIndirectFanoutRoutine;
		public FanoutRoutine lastFanoutRoutine;
		public EnvironmentFanoutRoutine lastEnvironmentFanoutRoutine;
		public CallArgument[] callArguments;
		
		private void reset() {
			this.lastIndirectFanoutLabel = null;
			this.lastFanoutLabel = null;
			this.lastIndirectFanoutRoutine = null;
			this.lastEnvironmentFanoutRoutine = null;
			this.lastFanoutRoutine = null;
			this.callArguments = null;
		}
		
		private EntryId getFanout() {
			if ((this.lastFanoutLabel == null) && (this.lastFanoutRoutine == null)) {
				return null;
			}
			if ((this.lastIndirectFanoutLabel != null) || (this.lastIndirectFanoutRoutine != null) || this.lastEnvironmentFanoutRoutine != null) {
				return null;
			}
			String label = (this.lastFanoutLabel == null) ? null : this.lastFanoutLabel.getValue();
			String routine = (this.lastFanoutRoutine == null) ? null : this.lastFanoutRoutine.getName();
		
			return new EntryId(routine, label);
		}
	}
		
	private Map<LineLocation, List<EntryId>> fanouts;
	private LastInfo lastInfo = new LastInfo();
	private Filter<EntryId> filter;
	private boolean conditional;

	public FanoutRecorder() {
	}
	
	public FanoutRecorder(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	@Override
	protected void passLocalByRef(Local local, int index) {
		String name = local.getName().toString();
		CallArgument ca = new CallArgument(CallArgumentType.LOCAL_BY_REF, name);
		this.lastInfo.callArguments[index] = ca;
	}

	@Override
	protected void visitActualList(ActualList actualList) {
		this.lastInfo.callArguments = new CallArgument[actualList.size()];
		super.visitActualList(actualList);
	}

	@Override
	protected void visitIndirectFanoutLabel(IndirectFanoutLabel label) {
		this.lastInfo.lastIndirectFanoutLabel = label;
		super.visitIndirectFanoutLabel(label);
	}
		
	@Override
	protected void visitFanoutLabel(FanoutLabel label) {
		this.lastInfo.lastFanoutLabel = label;
		super.visitFanoutLabel(label);
	}
		
	@Override
	protected void visitIndirectFanoutRoutine(IndirectFanoutRoutine routine) {
		this.lastInfo.lastIndirectFanoutRoutine = routine;
		super.visitIndirectFanoutRoutine(routine);
	}
		
	@Override
	protected void visitEnvironmentFanoutRoutine(EnvironmentFanoutRoutine routine) {
		this.lastInfo.lastEnvironmentFanoutRoutine = routine;
		super.visitEnvironmentFanoutRoutine(routine);
	}
		
	@Override
	protected void visitFanoutRoutine(FanoutRoutine routine) {
		this.lastInfo.lastFanoutRoutine = routine;
		super.visitFanoutRoutine(routine);
	}
	
	protected EntryId getLastFanout() {
		return this.lastInfo.getFanout();
	}
	
	protected CallArgument[] getLastArguments() {
		return this.lastInfo.callArguments;
	}
	
	protected void updateFanout(boolean isGoto, boolean conditional) {
		EntryId fanout = this.lastInfo.getFanout();
		if (fanout != null) {
			if (this.filter != null) {
				if (! this.filter.isValid(fanout)) return;
			}			
			LineLocation location = this.getLastLocation();
			List<EntryId> fanoutsOnLocation = this.fanouts.get(location);
			if (fanoutsOnLocation == null) {
				fanoutsOnLocation = new ArrayList<EntryId>();
				this.fanouts.put(location, fanoutsOnLocation);
			}
			fanoutsOnLocation.add(fanout);
		}		
	}
		
	@Override
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.lastInfo.reset();
		super.visitAtomicDo(atomicDo);
		boolean b = this.conditional || atomicDo.getPostConditional();
		this.updateFanout(false, b);
	}
	
	@Override
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		this.lastInfo.reset();
		super.visitAtomicGoto(atomicGoto);
		boolean b = this.conditional || atomicGoto.getPostConditional();
		this.updateFanout(true, b);
	}
	
	@Override
	protected void visitExtrinsic(Extrinsic extrinsic) {
		LastInfo current = this.lastInfo;
		this.lastInfo = new LastInfo();
		super.visitExtrinsic(extrinsic);
		this.updateFanout(false, false);
		this.lastInfo = current;
	}
	
	@Override
	protected void visitDo(Do d) {
		this.conditional = d.getPostCondition() != null;
		d.acceptSubNodes(this);
		this.conditional = false;
	}
	
	@Override
	protected void visitGoto(Goto g) {
		this.conditional = g.getPostCondition() != null;
		g.acceptSubNodes(this);
		this.conditional = false;		
	}

	@Override
	protected void visitRoutine(Routine routine) {
		this.fanouts = new HashMap<LineLocation, List<EntryId>>();
		super.visitRoutine(routine);
	}

	public Map<LineLocation, List<EntryId>> getRoutineFanouts() {
		return this.fanouts;
	}
	
	public void setFilter(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	public Map<LineLocation, List<EntryId>> getFanouts(Routine routine) {
		routine.accept(this);
		return this.fanouts;
	}		
}