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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.ActualList;
import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.EnvironmentFanoutRoutine;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.FanoutLabel;
import com.raygroupintl.m.parsetree.FanoutRoutine;
import com.raygroupintl.m.parsetree.IndirectFanoutLabel;
import com.raygroupintl.m.parsetree.IndirectFanoutRoutine;
import com.raygroupintl.m.parsetree.InnerEntryList;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.NumberLiteral;
import com.raygroupintl.m.parsetree.ObjectMethodCall;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.StringLiteral;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.EntryFanoutInfo;
import com.raygroupintl.m.struct.FanoutTypeENUM;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Filter;

public class FanoutRecorder extends LocationMarker {
	
	private class LastInfo {
		public IndirectFanoutLabel lastIndirectFanoutLabel;
		public FanoutLabel lastFanoutLabel;
		public IndirectFanoutRoutine lastIndirectFanoutRoutine;
		public FanoutRoutine lastFanoutRoutine;
		public EnvironmentFanoutRoutine lastEnvironmentFanoutRoutine;
		public CallArgument[] callArguments;
		public FanoutTypeENUM fanoutType;
		
		private void reset() {
			this.lastIndirectFanoutLabel = null;
			this.lastFanoutLabel = null;
			this.lastIndirectFanoutRoutine = null;
			this.lastEnvironmentFanoutRoutine = null;
			this.lastFanoutRoutine = null;
			this.callArguments = null;
			this.fanoutType = null;
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
		
		private EntryFanoutInfo getFanout2() {
			return new EntryFanoutInfo(getLastLocation(), fanoutType, getFanout());
		}
	}
		
	private Map<LineLocation, List<EntryId>> fanouts;
	private LinkedList<EntryFanoutInfo> fanouts2;
	private LastInfo lastInfo = new LastInfo();
	private Filter<EntryId> filter;
	private Set<Integer> innerEntryListHash = new HashSet<Integer>();

	public FanoutRecorder() {
	}
	
	public FanoutRecorder(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	@Override
	protected void passStringLiteral(StringLiteral literal, int index) {		
		String name = literal.getValue();
		CallArgument ca = new CallArgument(CallArgumentType.STRING_LITERAL, name);
		this.lastInfo.callArguments[index] = ca;
	}
	
	@Override
	protected void passNumberLiteral(NumberLiteral literal, int index) {		
		String name = literal.getValue();
		CallArgument ca = new CallArgument(CallArgumentType.NUMBER_LITERAL, name);
		this.lastInfo.callArguments[index] = ca;
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
	
	protected void updateFanout() {
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
		
		fanouts2.add(this.lastInfo.getFanout2());
	}
		
	@Override
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.lastInfo.reset();
		super.visitAtomicDo(atomicDo);
		this.lastInfo.fanoutType = FanoutTypeENUM.DO;
		this.updateFanout();
	}
	
	@Override
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		this.lastInfo.reset();
		super.visitAtomicGoto(atomicGoto);
		this.lastInfo.fanoutType = FanoutTypeENUM.GOTO;
		this.updateFanout();
	}
	
	@Override
	protected void visitExtrinsic(Extrinsic extrinsic) {
		LastInfo current = this.lastInfo;
		this.lastInfo = new LastInfo();
		super.visitExtrinsic(extrinsic);
		this.lastInfo.fanoutType = FanoutTypeENUM.EXTRINSIC;
		this.updateFanout();
		this.lastInfo = current;
	}
	
	@Override
	protected void visitObjectMethodCall(ObjectMethodCall omc) {
		LastInfo current = this.lastInfo;
		this.lastInfo = new LastInfo();
		super.visitObjectMethodCall(omc);
		//TODO: regist this as a fanout type?
		this.lastInfo = current;
	}
	
	@Override
	protected void visitInnerEntryList(InnerEntryList entryList) {
		int id = System.identityHashCode(entryList);
		if (! this.innerEntryListHash.contains(id)) {
			this.innerEntryListHash.add(id);
			super.visitInnerEntryList(entryList);
		}
	}
		
	@Override
	protected void visitRoutine(Routine routine) {
		this.fanouts = new HashMap<LineLocation, List<EntryId>>();
		this.fanouts2 = new LinkedList<EntryFanoutInfo>();
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
	
	public LinkedList<EntryFanoutInfo> getFanouts2(Routine routine) {
		routine.accept(this);
		return this.fanouts2;
	}
}