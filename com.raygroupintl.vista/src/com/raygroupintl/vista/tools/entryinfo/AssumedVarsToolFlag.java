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

package com.raygroupintl.vista.tools.entryinfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssumedVarsToolFlag {
	private List<String> expectedAssumedVariables;
	private boolean ignoreEntryWithNoAssumedVariables;
	
	public void addExpectedAssumeVariable(String variable) {
		if (this.expectedAssumedVariables == null) {
			this.expectedAssumedVariables = new ArrayList<String>();
		}
		this.expectedAssumedVariables.add(variable);
	}
	
	public void addAllExpectedAssumeVariables(List<String> variables) {
		if (this.expectedAssumedVariables == null) {
			this.expectedAssumedVariables = new ArrayList<String>();
		}
		this.expectedAssumedVariables.addAll(variables);
	}
	
	public List<String> getExpectedAssumedVariables() {
		if (this.expectedAssumedVariables == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.expectedAssumedVariables);
		}
	}
	
	public void setIgnoreEntryWithNoAssumedVariables(boolean b) {
		this.ignoreEntryWithNoAssumedVariables = b;
	}
	
	public boolean getIgnoreEntryWithNoAssumedVariables() {
		return this.ignoreEntryWithNoAssumedVariables;		
	}
}
