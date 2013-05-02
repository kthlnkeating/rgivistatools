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

package com.raygroupintl.m.tool.entry.legacycodeinfo;

import java.util.Set;

import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfo;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfoTR;

public class LegacyCodeInfo {
	public AssumedVariables assumedVariables;
	public BasicCodeInfoTR basicCodeInfo;

	public LegacyCodeInfo(AssumedVariables assumedVariables, BasicCodeInfoTR basicCodeInfo) {
		this.assumedVariables = assumedVariables;
		this.basicCodeInfo = basicCodeInfo;
	}
	
	public String[] getFormals() {
		return this.basicCodeInfo.getFormals();
	}
	
	public Set<String> getAssumedVariables() {
		return this.assumedVariables.toSet();
	}
	
	public BasicCodeInfo getBasicCodeInfo() {
		return this.basicCodeInfo.getData();
	}
	
	public AssumedVariables getAssumedVariablesTR() {
		return this.assumedVariables;
	}
	
	public BasicCodeInfoTR getBasicCodeInfoTR() {
		return this.basicCodeInfo;
	}
}
