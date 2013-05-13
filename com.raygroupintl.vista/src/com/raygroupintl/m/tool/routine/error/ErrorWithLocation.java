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

package com.raygroupintl.m.tool.routine.error;

import java.io.IOException;

import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.tool.OutputFlags;
import com.raygroupintl.m.tool.ToolResult;
import com.raygroupintl.output.Terminal;

public class ErrorWithLocation implements ToolResult {
	private MError error;
	private LineLocation location;
	
	public ErrorWithLocation(MError object, LineLocation location) {
		this.error = object;
		this.location = location;
	}
	
	public MError getObject() {
		return this.error;
	}
	
	public LineLocation getLocation() {
		return this.location;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public void write(Terminal t, OutputFlags flags) throws IOException {
		LineLocation location = this.getLocation();
		String offset = (location.getOffset() == 0 ? "" : '+' + String.valueOf(location.getOffset()));
		t.writeIndented(location.getTag() + offset + " --> " + this.getObject().getText());
	}
}
