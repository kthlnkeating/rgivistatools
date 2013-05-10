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

package com.raygroupintl.m.tool.routine.fanout;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.Label;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.m.parsetree.visitor.LocationMarker;
import com.raygroupintl.m.tool.RoutineEntryLinks;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.PassFilter;

public class FanoutRecorder extends LocationMarker {
	private String routineName;
	private RoutineEntryLinks routineFanouts;
	private Set<EntryId> entryFanouts;
	private Filter<EntryId> filter = new PassFilter<EntryId>();
	
	public void setFilter(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	protected void updateFanout(EntryId fanoutId, FanoutType type) {
		if (fanoutId != null) {
			EntryId f = fanoutId.getFullCopy(this.routineName);
			if (this.filter.isValid(f)) {
				this.entryFanouts.add(f);
			}
		}
	}

	@Override
	protected void visitAtomicDo(AtomicDo atomicDo) {
		super.visitAtomicDo(atomicDo);		
		this.updateFanout(atomicDo.getFanoutId(), FanoutType.DO);
	}
	
	@Override
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		super.visitAtomicGoto(atomicGoto);
		this.updateFanout(atomicGoto.getFanoutId(), FanoutType.GOTO);
	}
	
	@Override
	protected void visitExtrinsic(Extrinsic extrinsic) {
		super.visitExtrinsic(extrinsic);
		this.updateFanout(extrinsic.getFanoutId(), FanoutType.EXTRINSIC);
	}
	
	@Override
	protected void visitAssumedGoto(Label fromEntry, Label toEntry) {
		super.visitAssumedGoto(fromEntry, toEntry);
		String tag = toEntry.getName();
		EntryId assumedGoto = new EntryId(null, tag);
		this.updateFanout(assumedGoto, FanoutType.ASSUMED_GOTO);
	}
	
	@Override
	protected void visitEntry(Entry entry) {
		this.entryFanouts = new HashSet<EntryId>();
		super.visitEntry(entry);
		this.routineFanouts.put(entry.getName(), this.entryFanouts);
	}
			
	@Override
	protected void visitRoutine(Routine routine) {
		this.routineFanouts = new RoutineEntryLinks();
		this.routineName = routine.getName();
		super.visitRoutine(routine);
	}
	
	public RoutineEntryLinks getFanouts(Routine routine) {
		routine.accept(this);
		return this.routineFanouts;
	}
}