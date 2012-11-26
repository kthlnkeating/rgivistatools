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

package com.raygroupintl.m.parsetree.data;

public abstract class BlocksSupply<T> {
	public abstract Blocks<T> getBlocks(String routineName);
	
	public Block<T> getBlock(EntryId entryId) {
		String routineName = entryId.getRoutineName();
		Blocks<T> rbs = this.getBlocks(routineName);
		if (rbs != null) {
			String label = entryId.getLabelOrDefault();
			return rbs.get(label);
		} 
		return null;		
	}
}
