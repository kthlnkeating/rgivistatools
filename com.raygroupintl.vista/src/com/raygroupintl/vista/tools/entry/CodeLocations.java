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

package com.raygroupintl.vista.tools.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.raygroupintl.m.parsetree.data.AdditiveDataHandler;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.MEntryToolResult;

public class CodeLocations implements MEntryToolResult, Serializable, AdditiveDataHandler<CodeLocations> {
	private static final long serialVersionUID = 1L;

	private List<CodeLocation> codeLocations;

	public void add(CodeLocation codeLocation) {
		if (this.codeLocations == null) {
			this.codeLocations = new ArrayList<CodeLocation>();
		} 
		this.codeLocations.add(codeLocation);
	}
	
	public List<CodeLocation> getCodeLocations() {
		if (this.codeLocations == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.codeLocations);
		}
	}
	
	@Override
	public CodeLocations getNewInstance() {
		return new CodeLocations();
	}
	
	@Override
	public void update(CodeLocations target) {
		if (this.codeLocations != null) {
			for (CodeLocation c : this.codeLocations) {
				target.add(c);
			}
		}
	}
	
	public boolean isIdenticalTo(CodeLocation[] rhs) {
		if (rhs.length == (this.codeLocations == null ? 0 : this.codeLocations.size())) {
			if (rhs.length == 0) return true;			
			int i = 0;
			for (CodeLocation c : this.codeLocations) {
				if (! rhs[i].equals(c)) return false;
				++i;
			}
			return true;
		}		
		return false;
	}

	@Override
	public boolean isValid() {
		return this.codeLocations != null;
	}
	
	@Override
	public boolean isEmpty() {
		return (this.codeLocations == null) || (this.codeLocations.size() ==0);
	}
	
}
