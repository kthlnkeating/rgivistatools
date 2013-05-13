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

import java.util.Map;

import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.entry.MEntryToolInput;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfoToolParams;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeLocations;
import com.raygroupintl.m.tool.entry.fanout.EntryFanouts;
import com.raygroupintl.m.tool.entry.fanout.FanoutTool;
import com.raygroupintl.m.tool.entry.legacycodeinfo.LegacyCodeInfo;
import com.raygroupintl.m.tool.entry.legacycodeinfo.LegacyCodeInfoTool;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentTool;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentToolParams;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;
import com.raygroupintl.vista.tools.Tool;
import com.raygroupintl.vista.tools.Tools;

public class CLIEntryTools extends Tools {
	private static class EntryCodeInfoTool extends CLIEntryTool<LegacyCodeInfo> {	
		public EntryCodeInfoTool(CLIParams params) {
			super(params);
		}
		
		@Override
		public MEntryToolResult<LegacyCodeInfo> getResult(MEntryToolInput input) {
			RepositoryInfo ri = CLIParamsAdapter.getRepositoryInfo(this.params);
			AssumedVariablesToolParams p = CLIETParamsAdapter.toAssumedVariablesToolParams(this.params);
			BasicCodeInfoToolParams p2 = CLIETParamsAdapter.toBasicCodeInfoToolParams(this.params, ri);
			LegacyCodeInfoTool a = new LegacyCodeInfoTool(p, p2);
			return a.getResult(input);			
		}
	}

	private static class EntryLocalAssignmentTool extends CLIEntryTool<CodeLocations> {	
		public EntryLocalAssignmentTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected MEntryToolResult<CodeLocations> getResult(MEntryToolInput input) {
			LocalAssignmentToolParams params = CLIETParamsAdapter.toLocalAssignmentToolParams(this.params);
			LocalAssignmentTool a = new LocalAssignmentTool(params);
			return a.getResult(input);			
		}
	}

	private static class EntryFanoutTool extends CLIEntryTool<EntryFanouts> {	
		public EntryFanoutTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected MEntryToolResult<EntryFanouts> getResult(MEntryToolInput input) {
			CommonToolParams params = CLIETParamsAdapter.toCommonToolParams(this.params);
			FanoutTool a = new FanoutTool(params);
			return a.getResult(input);			
		}
	}

	public CLIEntryTools(String name) {
		super(name);
	}
	
	@Override
	protected void updateTools(Map<String, MemberFactory> tools) {
		tools.put("info", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryCodeInfoTool(params);
			}
		});
		tools.put("assumedvar", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new CLIAssumedVariablesTool(params);
			}
		});
		tools.put("localassignment", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryLocalAssignmentTool(params);
			}
		});
		tools.put("fanout", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryFanoutTool(params);
			}
		});
		tools.put("fanin", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new CLIFaninTool(params);
			}
		});
		tools.put("quittype", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new CLIQuitTypeTool(params);
			}
		});		
	}
}
