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

package com.raygroupintl.vista.tools.fnds;

import java.util.ArrayList;
import java.util.List;

import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;

public class ToolResultCollection<T extends ToolResult> implements ToolResult {
	private List<T> content = new ArrayList<T>(); 
	
	public void add(T toolResult) {
		this.content.add(toolResult);
	}
	
	@Override
	public void write(Terminal t, TerminalFormatter tf) {
		for (T eci : this.content) {
			eci.write(t, tf);
		}
	}
}
