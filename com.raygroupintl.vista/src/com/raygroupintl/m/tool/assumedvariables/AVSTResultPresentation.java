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

package com.raygroupintl.m.tool.assumedvariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AVSTResultPresentation {
	private List<String> expected;
	private boolean skipEmpty;

	public void addExpected(String variable) {
		if (this.expected == null) {
			this.expected = new ArrayList<String>();
		}
		this.expected.add(variable);
	}

	public void addExpected(List<String> variables) {
		if (this.expected == null) {
			this.expected = new ArrayList<String>();
		}
		this.expected.addAll(variables);
	}

	public List<String> getExpected() {
		if (this.expected == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.expected);
		}
	}

	public void setSkipEmpty(boolean b) {
		this.skipEmpty = b;
	}

	public boolean getSkipEmpty() {
		return this.skipEmpty;		
	}

}
