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

package com.raygroupintl.vista.tools;

import java.util.Set;
import java.util.Map;

public abstract class RunTypes {
	private Map<String, RunType.Factory> runTypes;
	
	protected RunTypes() {
		this.runTypes = this.createRunTypes();
	}

	protected abstract Map<String, RunType.Factory> createRunTypes();

	public Set<String> getRunTypeOptions() {
		return this.runTypes.keySet();
	}
		
	public RunType getRunType(String runTypeOption, CLIParams params) {
		RunType.Factory specifiedFactory = this.runTypes.get(runTypeOption);
		if (specifiedFactory == null) {
			return null;
		}
		return specifiedFactory.getInstance(params);			
	}	
}
