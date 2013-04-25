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

import com.raygroupintl.m.tool.RecursionDepth;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;

public class CLIParamsAdapter {
	public static RecursionSpecification toRecursionSpecification(CLIParams params) {
		String rdName = params.recursionDepth;
		if ((rdName == null) || (rdName.isEmpty())) {
			rdName = "label";
		}
		RecursionSpecification rs = new RecursionSpecification();
		try {
			RecursionDepth d = RecursionDepth.get(rdName);
			rs.setDepth(d);
			rs.addIncludedFanoutNamespaces(params.includeNamespaces);
			rs.addExcludedFanoutNamespaces(params.excludeNamespaces);
			rs.addExcludedFanoutExceptionNamespaces(params.excludeExceptionNamespaces);
			return rs;
		} catch (Exception ex) {
			return null;
		}			
	}
	
	public static AssumedVariablesToolParams toAssumedVariablesToolParams(CLIParams params) {
		AssumedVariablesToolParams result = new AssumedVariablesToolParams();
		if (params.excludes.size() > 0) {
			result.addExpected(params.excludes);
		}
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}
}
