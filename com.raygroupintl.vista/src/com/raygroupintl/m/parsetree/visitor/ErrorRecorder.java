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
import java.util.List;
import java.util.Set;

import com.raygroupintl.m.parsetree.EntryList;
import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;

public class ErrorRecorder extends LocationMarker {
	private List<ObjectInRoutine<MError>> result;
	private Set<LineLocation> exemptions;
	private boolean onlyFatal;
	
	private void addError(MError error) {
		if ((! this.onlyFatal) || (error.isFatal())) {
			LineLocation location = this.getLastLocation();
			if ((this.exemptions == null) || (! this.exemptions.contains(location))) {
				ObjectInRoutine<MError> element = new ObjectInRoutine<MError>(error, location);  
				this.result.add(element);
			}
		}
	}
	
	public ErrorRecorder(Set<LineLocation> exemptions) {
		this.exemptions = exemptions;
	}
	
	public ErrorRecorder() {
	}
	
	public void setOnlyFatal(boolean b) {
		this.onlyFatal = b;
	}

	@Override
	protected void visitErrorNode(ErrorNode errorNode) {		
		MError error = errorNode.getError();
		this.addError(error);
	}
		
	protected void visitDoBlock(DoBlock doBlock) {
		EntryList block = doBlock.getEntryList();
		if (block == null) {
			MError error = new MError(MError.ERR_NO_DO_BLOCK);
			this.addError(error);
		}
		super.visitDoBlock(doBlock);
	}
	
	public List<ObjectInRoutine<MError>> visitErrors(Routine routine) {
		this.result = new ArrayList<ObjectInRoutine<MError>>();		
		routine.accept(this);
		return this.result;
	}
}
