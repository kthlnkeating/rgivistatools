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

package com.raygroupintl.m.tool.entry;

public enum RecursionDepth {
	LABEL("label"),
	ROUTINE("routine"),
	ALL("all");
	
	private String name;

	private RecursionDepth(String name) {
		this.name = name;
	}

	public static RecursionDepth get(String name) throws IllegalArgumentException {
		for (RecursionDepth rd : RecursionDepth.values()) {
			if (name.equals(rd.name)) {
				return rd;
			}
		}
		throw new IllegalArgumentException(name);
	}
}