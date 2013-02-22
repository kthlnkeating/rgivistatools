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

package com.raygroupintl.m.struct;

public class CodeLocation {
	private String routineName;
	private LineLocation lineLocation;
	
	public CodeLocation(String routineName, LineLocation lineLocation) {
		this.routineName = routineName;
		this.lineLocation = lineLocation;
	}

	public CodeLocation(String routineName, String tag, int offset) {
		this(routineName, new LineLocation(tag, offset));
	}
	
	public String getRoutineName() {
		return this.routineName;
	}
	
	public LineLocation getLineLocation() {
		return this.lineLocation;
	}

	@Override
	public String toString() {
		return this.routineName + ": " + lineLocation.toString();
	}

	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof CodeLocation)) {	
			CodeLocation r = (CodeLocation) rhs;
			return this.routineName.equals(r.routineName) && (this.lineLocation.equals(r.lineLocation)); 
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String hashString = this.lineLocation.toString() + "^" + this.routineName;
		int result = hashString.hashCode(); 
		return result;
	}
	
}
