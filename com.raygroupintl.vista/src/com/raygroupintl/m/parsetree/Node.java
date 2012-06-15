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

package com.raygroupintl.m.parsetree;

public interface Node {
	void accept(Visitor visitor);
	
	void acceptPreAssignment(Visitor visitor);
	void acceptPostAssignment(Visitor visitor);
	
	void acceptExclusiveNew(Visitor visitor);
	void acceptNew(Visitor visitor);
	
	void acceptExclusiveKill(Visitor visitor);
	void acceptKill(Visitor visitor);

	void acceptPreMerge(Visitor visitor);
	void acceptPostMerge(Visitor visitor);
	
	ParentNode addSelf(ParentNode current, NodeList<Node> nodes);
	
	boolean setEntryList(EntryList entryList);
}
