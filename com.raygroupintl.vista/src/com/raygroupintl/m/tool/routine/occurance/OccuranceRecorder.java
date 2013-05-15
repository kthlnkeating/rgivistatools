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

package com.raygroupintl.m.tool.routine.occurance;

import java.util.EnumSet;
import java.util.List;

import com.raygroupintl.m.parsetree.Indirection;
import com.raygroupintl.m.parsetree.IntrinsicFunction;
import com.raygroupintl.m.parsetree.KillCmdNodes;
import com.raygroupintl.m.parsetree.NakedGlobal;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.tool.ResultsByLabel;
import com.raygroupintl.m.tool.ResultsByLabelVisitor;

class OccuranceRecorder extends ResultsByLabelVisitor<Occurance, List<Occurance>> {
	private EnumSet<OccuranceType> types;
	
	public void setRequestedTypes(EnumSet<OccuranceType> types) {
		this.types = types;
	}
	
	private void addOccurance(OccuranceType type) {
		if (this.types.contains(type)) {
			int index = this.getIndex();
			Occurance o = new Occurance(type, index);
			this.addResult(o);		
		}
	}
	
	@Override
	protected void visitIndirection(Indirection indirection) {
		this.addOccurance(OccuranceType.INDIRECTION);
		super.visitIndirection(indirection);
	}

	@Override
	protected void visitNakedGlobal(NakedGlobal global) {
		this.addOccurance(OccuranceType.NAKED_GLOBAL);
		super.visitNakedGlobal(global);
	}
	
	@Override
	protected void visitIntrinsicFunction(IntrinsicFunction intrinsicFunction) {
		String name = intrinsicFunction.getName().toUpperCase();
		if (name.equals("T") || name.equals("TEXT")) {
				this.addOccurance(OccuranceType.INTRINSIC_TEXT);
		}
		super.visitIntrinsicFunction(intrinsicFunction);
	}
	
	@Override
	protected void visitReadCmd(ReadCmd readCmd) {
		this.addOccurance(OccuranceType.READ);
		super.visitReadCmd(readCmd);
	}

	
	@Override
	protected void visitWriteCmd(WriteCmd writeCmd) {
		this.addOccurance(OccuranceType.WRITE);
		super.visitWriteCmd(writeCmd);
	}
	
	@Override
	protected void visitXecuteCmd(XecuteCmd xecuteCmd) {
		this.addOccurance(OccuranceType.EXECUTE);
		super.visitXecuteCmd(xecuteCmd);
	}

	@Override
	protected void visitAllKillCmd(KillCmdNodes.AllKillCmd atomicKill) {
		this.addOccurance(OccuranceType.EXCLUSIVE_KILL);
		super.visitAllKillCmd(atomicKill);
	}
	
	@Override
	protected void visitExclusiveAtomicKill(KillCmdNodes.ExclusiveAtomicKill atomicKill) {
		this.addOccurance(OccuranceType.EXCLUSIVE_KILL);
		super.visitExclusiveAtomicKill(atomicKill);
	}

	@Override
	protected ResultsByLabel<Occurance, List<Occurance>> getNewResultsByLabelInstance() {
		return new OccurancesByLabel();
	}
}
