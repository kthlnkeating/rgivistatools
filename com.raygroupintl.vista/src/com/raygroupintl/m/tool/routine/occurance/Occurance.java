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

package com.raygroupintl.m.tool.routine.occurance;

import java.io.IOException;

import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ToolResult;
import com.raygroupintl.output.Terminal;

public class Occurance implements ToolResult {
	private OccuranceType type;
	private int lineIndex;
	
	public  Occurance(OccuranceType type, int lineIndex) {
		this.type = type;
		this.lineIndex = lineIndex;
	}
	
	public OccuranceType getType() {
		return this.type;
	}
	
	public int getLineIndex() {
		return this.lineIndex;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public void write(Terminal t, OutputFlags flags) throws IOException {
		int lineIndex = this.getLineIndex();
		String location = "Line " + String.valueOf(lineIndex);
		String type = this.getType().toString();
		t.writeIndented(location + " --> " + type);
	}
}
