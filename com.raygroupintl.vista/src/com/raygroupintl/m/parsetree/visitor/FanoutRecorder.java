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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.ActualList;
import com.raygroupintl.m.parsetree.AtomicDo;
import com.raygroupintl.m.parsetree.AtomicGoto;
import com.raygroupintl.m.parsetree.Extrinsic;
import com.raygroupintl.m.parsetree.InnerEntryList;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.NumberLiteral;
import com.raygroupintl.m.parsetree.ObjectMethodCall;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.StringLiteral;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.struct.Filter;

public class FanoutRecorder extends LocationMarker {
	private Map<LineLocation, List<EntryId>> fanouts;
	private Filter<EntryId> filter;
	private Set<Integer> innerEntryListHash = new HashSet<Integer>();
	private CallArgument[] lastCallArguments;
	
	public FanoutRecorder() {
	}
	
	public FanoutRecorder(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	@Override
	protected void passStringLiteral(StringLiteral literal, int index) {		
		String name = literal.getValue();
		CallArgument ca = new CallArgument(CallArgumentType.STRING_LITERAL, name);
		this.lastCallArguments[index] = ca;
	}
	
	@Override
	protected void passNumberLiteral(NumberLiteral literal, int index) {		
		String name = literal.getValue();
		CallArgument ca = new CallArgument(CallArgumentType.NUMBER_LITERAL, name);
		this.lastCallArguments[index] = ca;
	}
	
	@Override
	protected void passLocalByRef(Local local, int index) {
		String name = local.getName().toString();
		CallArgument ca = new CallArgument(CallArgumentType.LOCAL_BY_REF, name);
		this.lastCallArguments[index] = ca;
	}

	@Override
	protected void visitActualList(ActualList actualList) {
		this.lastCallArguments = new CallArgument[actualList.size()];
		super.visitActualList(actualList);
	}

	protected CallArgument[] getLastArguments() {
		return this.lastCallArguments;
	}
	
	protected void updateFanout(EntryId fanoutId) {
		if (fanoutId != null) {
			if (this.filter != null) {
				if (! this.filter.isValid(fanoutId)) return;
			}			
			LineLocation location = this.getLastLocation();
			List<EntryId> fanoutsOnLocation = this.fanouts.get(location);
			if (fanoutsOnLocation == null) {
				fanoutsOnLocation = new ArrayList<EntryId>();
				this.fanouts.put(location, fanoutsOnLocation);
			}
			fanoutsOnLocation.add(fanoutId);
		}
	}
		
	@Override
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.lastCallArguments = null;
		super.visitAtomicDo(atomicDo);		
		this.updateFanout(atomicDo.getFanoutId());
	}
	
	@Override
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		this.lastCallArguments = null;
		super.visitAtomicGoto(atomicGoto);
		this.updateFanout(atomicGoto.getFanoutId());
	}
	
	@Override
	protected void visitExtrinsic(Extrinsic extrinsic) {
		CallArgument[] current = this.lastCallArguments;
		super.visitExtrinsic(extrinsic);
		this.updateFanout(extrinsic.getFanoutId());
		this.lastCallArguments = current;
	}
	
	@Override
	protected void visitObjectMethodCall(ObjectMethodCall omc) {
		CallArgument[] current = this.lastCallArguments;
		super.visitObjectMethodCall(omc);
		this.lastCallArguments = current;
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