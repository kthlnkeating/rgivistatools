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

import com.raygroupintl.m.parsetree.ErrorNode;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.Visitor;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.vista.struct.MError;
import com.raygroupintl.vista.struct.MLocationedError;

public class ErrorVisitor extends Visitor {
	private String tag = "";
	private int index;

	private List<MLocationedError> result;
	private Set<LineLocation> exemptions;
	
	@Override
	protected void visitErrorNode(ErrorNode errorNode) {		
		LineLocation location = new LineLocation(this.tag, this.index);
		if ((this.exemptions == null) || (! this.exemptions.contains(location))) {
			MError error = errorNode.getError();
			MLocationedError element = new MLocationedError(error, location);  
			this.result.add(element);
		}
	}
		
	@Override
	protected void visitLine(Line line) {
		this.tag = line.getTag();
		this.index = line.getIndex();
		super.visitLine(line);
	}
			
	public List<MLocationedError> visitErrors(Routine routine) {
		return this.visitErrors(routine, null);
	}
	
	public List<MLocationedError> visitErrors(Routine routine, Set<LineLocation> exemptions) {
		this.result = new ArrayList<MLocationedError>();		
		this.exemptions = exemptions;
		this.visitRoutine(routine);
		return this.result;
	}
}
