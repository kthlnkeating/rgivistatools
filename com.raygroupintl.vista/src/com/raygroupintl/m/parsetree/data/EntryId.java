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

package com.raygroupintl.m.parsetree.data;

public class EntryId {
	private String routineName;
	private String label;
	
	public EntryId(String routineName, String label) {
		this.routineName = routineName;
		this.label = label;
	}
	
	public String getRoutineName() {
		return routineName;
	}
	
	public String getTag() {
		return this.label;
	}
	
	public String getLabelOrDefault() {
		if (this.label == null) {
			return this.routineName;
		} else {
			return this.label;
		}
	}
	
	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof EntryId)) {	
			String lhsString = this.toString();
			String rhsString = rhs.toString();
			return lhsString.equals(rhsString);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = this.toString().hashCode(); 
		return result;
	}
	
	@Override
	public String toString() {
		String lbl = this.getTag();
		String rou = this.getRoutineName();
		if (rou != null) {
			rou = "^" + rou;
		} else {
			rou = "";
		}
		if (lbl == null) {
			lbl = "";
		}					
		return lbl + rou;		
	}
}
