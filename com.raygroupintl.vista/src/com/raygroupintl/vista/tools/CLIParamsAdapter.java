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

import java.io.IOException;

import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.RecursionDepth;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.SavedParsedTrees;
import com.raygroupintl.m.tool.SourceCodeFiles;
import com.raygroupintl.m.tool.SourceCodeToParseTreeAdapter;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.m.tool.basiccodeinfo.BasicCodeInfoToolParams;
import com.raygroupintl.m.tool.localassignment.LocalAssignmentToolParams;
import com.raygroupintl.vista.repository.RepositoryInfo;

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
	
	private static ParseTreeSupply getParseTreeSupply(CLIParams params) {
		if ((params.parseTreeDirectory == null) || params.parseTreeDirectory.isEmpty()) {
			String vistaFOIA = RepositoryInfo.getLocation();
			try {
				SourceCodeFiles files = SourceCodeFiles.getInstance(vistaFOIA);
				return new SourceCodeToParseTreeAdapter(files);
			}
			catch (IOException e) {
				return null;
			}				
		} else {
			return new SavedParsedTrees(params.parseTreeDirectory);
		}		
	}

	public static CommonToolParams toCommonToolParams(CLIParams params) {
		ParseTreeSupply pts = getParseTreeSupply(params);
		CommonToolParams result = new CommonToolParams(pts);
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;				
	}

	public static LocalAssignmentToolParams toLocalAssignmentToolParams(CLIParams params) {
		ParseTreeSupply pts = getParseTreeSupply(params);
		LocalAssignmentToolParams result = new LocalAssignmentToolParams(pts);
		if (params.includes.size() > 0) {
			result.addLocals(params.includes);
		}
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}

	public static AssumedVariablesToolParams toAssumedVariablesToolParams(CLIParams params, RepositoryInfo repositoryInfo) {
		ParseTreeSupply pts = getParseTreeSupply(params);
		AssumedVariablesToolParams result = new AssumedVariablesToolParams(pts, repositoryInfo);
		if (params.excludes.size() > 0) {
			result.addExpected(params.excludes);
		}
		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}

	public static BasicCodeInfoToolParams toBasicCodeInfoToolParams(CLIParams params, RepositoryInfo repositoryInfo) {
		ParseTreeSupply pts = getParseTreeSupply(params);
		BasicCodeInfoToolParams result = new BasicCodeInfoToolParams(pts, repositoryInfo);

		RecursionSpecification rs = toRecursionSpecification(params);
		result.setRecursionSpecification(rs);	
		return result;		
	}
}
