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

package com.raygroupintl.m.cmdtree;

public class EntryTag extends Block<Line> {
	private String tagName;
	private String[] parameters;
	
	public EntryTag(String tagName) {
		this.tagName = tagName;
	}
	
	public EntryTag(String tagName, String[] parameters) {
		this.parameters = parameters;
	}
		
	public String getKey() {
		return this.tagName;
	}
	
	public int getParameterCount() {
		return this.parameters.length;
	}
	
	public String getParameter(int index) {
		return this.parameters[index];
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visitEntryTag(this);
	}
}
