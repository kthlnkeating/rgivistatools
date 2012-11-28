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

package com.raygroupintl.vista.tools.entryfanin;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataHandler;

public class FaninMark implements RecursiveDataHandler<PathPieceToEntry> {
	private EntryId startNode;
	private EntryId endNode;
	
	public FaninMark(EntryId startNode) {
		this.startNode = startNode;			
	}
	
	public void set(EntryId endNode) {
		this.endNode = endNode;
	}
	
	@Override
	public PathPieceToEntry getLocalCopy()  {
		return new PathPieceToEntry(this.startNode);
	}
	
	@Override
	public int update(PathPieceToEntry target, PathPieceToEntry source, int sourceIndex) {
		int changeCount = 0;
		if (source.exist()) {
			EntryId sourceStart = source.getStartEntry();
			if (target.add(sourceStart)) {
				++changeCount;
			}
		} 
		if (this.endNode != null) {
			if (target.add(this.endNode)) {
				++changeCount;
			}
		}
		return changeCount;
	}
}
